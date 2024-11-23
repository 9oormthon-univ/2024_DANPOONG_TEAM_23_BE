//package com.doctorgc.doctorgrandchild.service;
//
//import static java.lang.System.getenv;
//
//import com.doctorgc.doctorgrandchild.dto.HospitalResponseDto.HospitalDetailsDto;
//import com.doctorgc.doctorgrandchild.dto.HospitalResponseDto.RecommendedHospitalDto;
//import com.doctorgc.doctorgrandchild.dto.HospitalResponseDto.RecommendedHospitalListDto;
//import com.doctorgc.doctorgrandchild.entity.DiagnosisResult;
//import com.doctorgc.doctorgrandchild.entity.hospital.Hospital;
//import com.doctorgc.doctorgrandchild.entity.hospital.RecommendedHospital;
//import com.doctorgc.doctorgrandchild.repository.DiagnosisResultRepository;
//import com.doctorgc.doctorgrandchild.repository.HospitalRepository;
//import com.doctorgc.doctorgrandchild.repository.RecommendedHospitalRepository;
//import java.net.URI;
//import java.util.ArrayList;
//import java.util.Base64;
//import java.util.List;
//import java.util.Map;
//import java.util.Objects;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;
//import org.springframework.web.util.UriComponentsBuilder;
//
//@RequiredArgsConstructor
//@Service
//public class HospitalService {
//    private final DiagnosisResultRepository diagnosisResultRepository;
//    private final HospitalRepository hospitalRepository;
//    private final RecommendedHospitalRepository recommendedHospitalRepository;
//
//    private static final String KAKAO_API_URL = "https://dapi.kakao.com/v2/local/search/keyword.json";
//    Map<String, String> env = getenv();
//    private String restApiKey = Base64.getEncoder().encodeToString(
//            Objects.requireNonNull(env.get("KAKAO_REST_API_KEY")).getBytes());
//
//
//
//
//    //추천 hospital 간략정보 리스트
//    public RecommendedHospitalListDto getHospitalLists (String user_x, String user_y,
//            DiagnosisResult diagnosisResult){
//        String keyword = diagnosisResultRepository.findHospitalCategoryByDiagnosisResultId(
//                diagnosisResult.getId());
//        RestTemplate restTemplate = new RestTemplate();
//
//        //URL 구성
//        URI uri = UriComponentsBuilder.fromHttpUrl(KAKAO_API_URL)
//                          .queryParam("page", 1) // 페이지 번호
//                          .queryParam("size", 4) // 한 페이지에 보여줄 결과 수
//                          .queryParam("sort", "distance") // 정렬 기준
//                          .queryParam("category_group_code", "HP8") // 카테고리 코드 (HP8: 병원)
//                          .queryParam("x", user_x) // 중심 좌표 (경도)
//                          .queryParam("y", user_y) // 중심 좌표 (위도)
//                          .queryParam("radius", 200) // 검색 반경
//                          .queryParam("query", keyword) // 검색 키워드
//                          .build()
//                          .toUri();
//        // Header 구성
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Authorization", "KakaoAK " + restApiKey);
//
//        // 요청 실행
//        ResponseEntity<Map> response = restTemplate.exchange(
//                uri,
//                HttpMethod.GET,
//                new org.springframework.http.HttpEntity<>(headers),
//                Map.class
//        );
//        saveHospitalDataFromKakao(response,diagnosisResult);
//
//
//
//
//
//        return;
//    }
//
//    //특정 hospital 상세조회 리스트
//    public HospitalDetailsDto getHospitalDetails (){
//
//        return;
//    }
//
//    //카카오로부터 받은 데이터 db에 저장
//    public void saveHospitalDataFromKakao(Map<String, Object> kakaoResponse,DiagnosisResult diagnosisResult) {
//        // Kakao API 응답 파싱
//        List<Map<String, Object>> documents = (List<Map<String, Object>>) kakaoResponse.get("documents");
//
//        for (Map<String, Object> doc : documents) {
//            // Hospital 엔티티 저장
//            Hospital hospital = hospitalRepository.save(Hospital.builder()
//                                                                .id((String) doc.get("id"))
//                                                                .name((String) doc.get("place_name"))
//                                                                .category((String) doc.get("category_name"))
//                                                                .address((String) doc.get("address_name"))
//                                                                .roadAddress((String) doc.get("road_address_name"))
//                                                                .phoneNumber((String) doc.get("phone"))
//                                                                .locationUrl((String) doc.get("place_url"))
//                                                                .x((String) doc.get("x"))
//                                                                .y((String) doc.get("y"))
//                                                                .build());
//
//            // RecommendedHospital 엔티티 저장
//            recommendedHospitalRepository.save(RecommendedHospital.builder()
//                                                       .hospital(hospital)
//                                                       .diagnosisResult(diagnosisResult)
//                                                       .build());
//        }
//    }
//}
//
//
