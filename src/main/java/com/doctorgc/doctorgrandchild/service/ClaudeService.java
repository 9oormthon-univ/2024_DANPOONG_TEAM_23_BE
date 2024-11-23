package com.doctorgc.doctorgrandchild.service;

import com.doctorgc.doctorgrandchild.dto.DiagnosisResponseDto.DiagnosisChatDto;
import com.doctorgc.doctorgrandchild.dto.DiagnosisResponseDto.DiagnosisResultContentsDto;
import com.doctorgc.doctorgrandchild.dto.DiagnosisResponseDto.DiagnosisResultDto;
import com.doctorgc.doctorgrandchild.dto.HealthChangesResponseDto.HealthChangesDto;
import com.doctorgc.doctorgrandchild.dto.HealthChangesResponseDto.ShortHealthChangesDto;
import com.doctorgc.doctorgrandchild.entity.DiagnosisResult;
import com.doctorgc.doctorgrandchild.entity.HealthReport;
import com.doctorgc.doctorgrandchild.entity.member.Member;
import com.doctorgc.doctorgrandchild.repository.DiagnosisResultRepository;
import com.doctorgc.doctorgrandchild.repository.HealthReportRepository;
import com.doctorgc.doctorgrandchild.repository.MemberRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.anthropic.AnthropicChatModel;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class ClaudeService {
    private final AnthropicChatModel chatModel;
    private final DiagnosisResultRepository diagnosisResultRepository;
    private final HealthReportRepository healthReportRepository;

    //한달 간의 건강 기록 데이터를 가져옴
    private String getMonthlyDiagnosis(Member member) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusMonths(1);
        List<DiagnosisResult> diagnosisResults =
            diagnosisResultRepository.findByMemberAndDateBetween(member, startDate, endDate);
        if (diagnosisResults.isEmpty()) {
            return "";
        }
        return diagnosisResults.stream()
            .map(diagnosisResult -> diagnosisResult.getDate() + ": " + diagnosisResult.getShortContent())
            .collect(Collectors.joining("\n"));
    }

    //내 건강 변화 결과 파싱
    private HealthChangesDto extractReportContent(String response) {
        int startDiseaseIndex = response.indexOf("<disease>");
        int endDiseaseIndex = response.indexOf("</disease>");

        String disease = startDiseaseIndex != -1 && endDiseaseIndex != -1
            ? response.substring(startDiseaseIndex + 9, endDiseaseIndex).trim()
            : "";

        int startReportIndex = response.indexOf("<report>");
        int endReportIndex = response.indexOf("</report>");

        String reportContent = startReportIndex != -1 && endReportIndex != -1
            ? response.substring(startReportIndex + 8, endReportIndex).trim()
            : "";

        return HealthChangesDto.builder()
            .reportContent(reportContent)
            .disease(disease)
            .build();
    }

    private DiagnosisResultContentsDto extractDiagnosisQuestion(String response) {

        String question = "";
        List<String> options = new ArrayList<>();

        Pattern questionPattern = Pattern.compile("<question>\"(.*?)\"</question>");
        Matcher questionMatcher = questionPattern.matcher(response);
        if (questionMatcher.find()) {
            question = questionMatcher.group(1);
        }

        Pattern optionsPattern = Pattern.compile("<options>\\[(.*?)\\]</options>");
        Matcher optionsMatcher = optionsPattern.matcher(response);
        if (optionsMatcher.find()) {
            String optionsStr = optionsMatcher.group(1);

            String[] optionArray = optionsStr.split("\",\\s*\"");
            for (String option : optionArray) {
                option = option.replaceAll("\"", "").trim();
                if (!option.isEmpty()) {
                    options.add(option);
                }
            }
        }

        return DiagnosisResultContentsDto.builder()
            .options(options)
            .result("")
            .question(question)
            .build();
    }

    private DiagnosisResultContentsDto extractDiagnosisResult(String response, DiagnosisResult diagnosisResult) {

        String result = "";
        String summary = "";
        String category = "";

        Pattern resultPattern = Pattern.compile("<diagnosisResult>\"(.*?)\"</diagnosisResult>");
        Matcher resultMatcher = resultPattern.matcher(response);
        if (resultMatcher.find()) {
            result = resultMatcher.group(1);
        }

        // Summary 추출
        Pattern summaryPattern = Pattern.compile("<diagnosisResultSummary>\"(.*?)\"</diagnosisResultSummary>");
        Matcher summaryMatcher = summaryPattern.matcher(response);
        if (summaryMatcher.find()) {
            summary = summaryMatcher.group(1);
        }

        // Hospital Category 추출
        Pattern categoryPattern = Pattern.compile("<hospitalCategory>(.*?)</hospitalCategory>");
        Matcher categoryMatcher = categoryPattern.matcher(response);
        if (categoryMatcher.find()) {
            category = categoryMatcher.group(1).trim();
        }


        return DiagnosisResultContentsDto.builder()
            .question("")
            .options(new ArrayList<>())
            .result(result)
            .build();

    }


    public ShortHealthChangesDto generateHealthChanges(Member member) {

        String basicPrompt = "You are an AI doctor tasked with analyzing health data for elderly patients. Your goal is to provide an easy-to-understand diagnostic report based on a month's worth of diagnostic records. Follow these instructions carefully:\n"
            + "\n"
            + "First, I will provide you with the patient's monthly diagnostic data:\n"
            + "\n"
            + "<diagnostic_data>\n"
            + "{{DIAGNOSTIC_DATA}}\n"
            + "</diagnostic_data>\n"
            + "\n"
            + "Analyze this data to create a health trend report for the patient. Follow these steps:\n"
            + "\n"
            + "1. Exclude any suspected disease diagnoses from previous diagnostic results in the provided data. Do not use these in your analysis.\n"
            + "\n"
            + "2. Analyze the symptoms and their frequency over the past month.\n"
            + "\n"
            + "3. Identify potential diseases based solely on the symptom data, not on any previous diagnoses.\n"
            + "\n"
            + "4. Create a report using the following markdown format:\n"
            + "\n"
            + "   1. **의심 질병:**\n"
            + "      - \"(의심 질병)이 의심됩니다.\"\n"
            + "\n"
            + "   2. **진단 기록 요약:**\n"
            + "      - \"최근 한 달간 (의심 질병) 관련 증상이 (진단 횟수)회 나타났습니다.\"\n"
            + "\n"
            + "   3. **주의사항:**\n"
            + "      - \"(증상이 악화되거나 추가 증상이 나타날 경우 주의할 점)\"\n"
            + "\n"
            + "   4. **건강 관리 팁:**\n"
            + "      - \"(증상 완화 및 예방을 위한 실질적인 조언)\"\n"
            + "\n"
            + "Additional requirements:\n"
            + "\n"
            + "1. Base your suspected disease selection solely on symptom information, excluding any previous disease diagnoses from the data.\n"
            + "\n"
            + "2. Consider symptoms that could be precursors to specific diseases, even if they don't occur simultaneously.\n"
            + "\n"
            + "3. Select suspected diseases based on both the frequency and variety of related precursor symptoms.\n"
            + "\n"
            + "4. Analyze the correlation between symptoms and their likelihood of being precursors to specific diseases.\n"
            + "\n"
            + "5. Track patterns of various symptoms accumulating over time that might be related to the same disease.\n"
            + "\n"
            + "6. Use a database of disease precursor symptoms to inform your analysis and disease selection.\n"
            + "\n"
            + "7. Keep your writing concise and clear, using markdown syntax to emphasize important points.\n"
            + "\n"
            + "CRITICAL INSTRUCTION FOR <disease> TAG:\n"
            + "- ONLY insert ONE clean, precise disease name in the <disease> tag\n"
            + "- NO symbols, special characters, or additional text\n"
            + "- SELECT the single most representative disease from your diagnostic analysis\n"
            + "- Use STANDARD medical terminology for the disease name\n"
            + "- Ensure the disease name is clear, specific, and matches recognized medical nomenclature\n"
            + "\n"
            + "Please provide your final report within <report> tags. The <disease> tag MUST contain ONLY one precise disease name.";
        String monthlyDiagnosis = getMonthlyDiagnosis(member);

        //한달 간의 건강 진단 내역이 없으면 빈 문자열 반환
        if (monthlyDiagnosis.isEmpty()) {
            return ShortHealthChangesDto.builder()
                .content(monthlyDiagnosis)
                .build();
        }

        String prompt = basicPrompt.replace("{{DIAGNOSTIC_DATA}}", monthlyDiagnosis);

        String response = chatModel.call(prompt);

        HealthChangesDto report = extractReportContent(response);

        HealthReport healthReport = HealthReport.builder()
            .disease(report.getDisease())
            .date(LocalDate.now())
            .member(member)
            .reportContent(report.getReportContent())
            .build();

        healthReportRepository.save(healthReport);

        return ShortHealthChangesDto.builder()
            .content(report.getDisease())
            .build();
    }

    public DiagnosisChatDto generateDiagnosis(Member member, DiagnosisResult diagnosisResult) {
        String basicPrompt = "You are an AI doctor specializing in diagnosing symptoms in elderly patients. Your task is to analyze the provided user data and generate an easy-to-understand diagnosis report. \n"
            + "\n"
            + "Here is the user data you will be working with:\n"
            + "<user_data>\n"
            + "{{USER_DATA}}\n"
            + "</user_data>\n"
            + "\n"
            + "The current question count is:\n"
            + "<question_count>\n"
            + "{{QUESTION_COUNT}}\n"
            + "</question_count>\n"
            + "\n"
            + "Analyze the user data carefully. If you determine that you need more information to make an accurate diagnosis, generate additional questions. If you have enough information to make a diagnosis, provide the final diagnosis result and summary.\n"
            + "\n"
            + "When generating additional questions:\n"
            + "1. Keep questions concise and easy to understand, within 15 characters.\n"
            + "2. Provide a maximum of 5 answer options.\n"
            + "3. Use markdown to **bold** important words or items in the question.\n"
            + "4. Format the question and options as follows:\n"
            + "   <question>\"Question content\"</question>\n"
            + "   <options>[\"Option 1\", \"Option 2\", \"Option 3\", \"Option 4\", \"Option 5\"]</options>\n"
            + "\n"
            + "When providing the final diagnosis result and summary:\n"
            + "1. Use markdown formatting.\n"
            + "2. Follow this structure for the diagnosis result:\n"
            + "   1. **Symptom Summary**:\n"
            + "      \"(User name)님이 느끼시는 (summarized symptoms) 분석 결과\"\n"
            + "   2. **Diagnosis Result**:\n"
            + "      \"(suspected illness)일 가능성이 높습니다.\"\n"
            + "   3. **Recommended Action**:\n"
            + "      \"(recommended medical department)를 하루 빨리 방문해보시길 권장합니다.\"\n"
            + "   4. **Precautions**:\n"
            + "      \"(precautions for worsening symptoms or additional symptoms)\"\n"
            + "   5. **Prevention and Management Tips**:\n"
            + "      \"(practical advice for symptom relief and prevention)\"\n"
            + "\n"
            + "3. Follow this structure for the diagnosis summary:\n"
            + "   (summarized symptoms) 증상이 있으셨어요.\n"
            + "   (suspected illness) 이 의심돼요.\n"
            + "\n"
            + "4. Format the diagnosis result, summary, and hospital category as follows:\n"
            + "   <diagnosisResult>\"Diagnosis result\"</diagnosisResult>\n"
            + "   <diagnosisResultSummary>\"Diagnosis result summary\"</diagnosisResultSummary>\n"
            + "   <hospitalCategory>Hospital category (only one)</hospitalCategory>\n"
            + "\n"
            + "Important requirements:\n"
            + "1. Base your response on the provided user data and any previous response data.\n"
            + "2. If the question count is 5, provide only the final diagnosis result, summary, and one hospital category to visit.\n"
            + "3. Use a user-friendly tone and concise language in your responses.\n"
            + "4. Always use markdown formatting where specified.";
        String medicalConditions = member.getMedicalConditions();
        String userData = "";
        if(medicalConditions != null) {
            userData += "사용자 기저질환: " + member.getMedicalConditions() + "\n";
        }
        userData += diagnosisResult.getUserInput() + "\n";
        String prompt = basicPrompt.replace("{{USER_DATA}}", userData);
        String finalPrompt = prompt.replace("{{QUESTION_COUNT}}",
            String.valueOf(diagnosisResult.getQuestionCount()));
        String response = chatModel.call(prompt);
        if (response.contains("<question>")) {
            return DiagnosisChatDto.builder()
                .isQuestion(true)
                .content(extractDiagnosisQuestion(response))
                .build();
        } else {
            return DiagnosisChatDto.builder()
                .isQuestion(false)
                .content(extractDiagnosisResult(response, diagnosisResult))
                .build();
        }
    }
}
