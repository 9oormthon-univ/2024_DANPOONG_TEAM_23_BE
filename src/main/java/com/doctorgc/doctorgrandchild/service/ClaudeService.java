package com.doctorgc.doctorgrandchild.service;

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
import java.util.List;
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
    private final MemberRepository memberRepository;
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
}
