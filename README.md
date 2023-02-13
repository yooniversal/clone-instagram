 
<div align="center">

# Yoonstagram: Instagram Clone Project

![HTML5](https://img.shields.io/badge/HTML5-E34F26?style=flat&logo=HTML5&logoColor=white)
![CSS3](https://img.shields.io/badge/CSS3-1572B6?style=flat&logo=CSS3&logoColor=white)
![JavaScript](https://img.shields.io/badge/JavaScript-F7DF1E?style=flat&logo=JavaScript&logoColor=white)
![jQuery](https://img.shields.io/badge/jQuery-0769AD?style=flat&logo=jQuery&logoColor=white)

![Java](https://img.shields.io/badge/Java-F80000?style=flat&logo=Oracle&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=flat&logo=SpringBoot&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring_Security-6DB33F?style=flat&logo=SpringSecurity&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=flat&logo=MySQL&logoColor=white)
![Thymeleaf](https://img.shields.io/badge/Thymeleaf-005F0F?style=flat&logo=Thymeleaf&logoColor=white)

![Amazon EC2](https://img.shields.io/badge/Amazon_EC2-FF9900?style=flat&logo=AmazonEC2&logoColor=white)
![Amazon RDS](https://img.shields.io/badge/Amazon_RDS-527FFF?style=flat&logo=AmazonRDS&logoColor=white)

</div>
<br>

### 📜 목적
- [강의](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81%EB%B6%80%ED%8A%B8-JPA-%ED%99%9C%EC%9A%A9-1)에서 배운 Spring 실습 내용을 클론 코딩을 통해 직접 적용해보는 경험 쌓기
- 이미 알고있는 서비스를 분석해보면서 DB schema를 어떻게 설계하는게 적절한지 생각해보기
- 설계한 API를 적용해 넘긴 데이터는 어떤 방식으로 활용하면서 렌더링할 수 있는지 확인
- 프론트는 [이 프로젝트](https://github.com/hyunmin0317/instagram-clone)를 참고하되 현재 인스타그램 디자인으로 가능한 만큼 발전시켜보기
- 다양한 사용자가 접근할 수 있도록 *AWS EC2*에 적용해보기
<br>

### 📆 기간
23.01.13 ~ 23.02.13
<br>
<br>

### 🎞 Preview

[여기](http://54.93.218.93/)에서 이용할 수 있습니다.


<br>

| 회원가입 및 로그인 | 프로필 편집 |
| --- | --- |
| ![로그인](https://user-images.githubusercontent.com/61930524/216841550-563111be-fb7c-4c60-99e6-6f127a126990.gif) |![프로필편집](https://user-images.githubusercontent.com/61930524/216841599-b57bb66b-ec73-434f-a260-a43972fb70d0.gif)

| 업로드 | 좋아요 |
| --- | --- |
| ![업로드](https://user-images.githubusercontent.com/61930524/216841678-fd0826cd-cacb-41a5-ac37-fe1b4d758113.gif) |![좋아요](https://user-images.githubusercontent.com/61930524/216841721-ccb33a89-6300-47aa-896b-c2724961d769.gif)|

| 알림 | 팔로잉 |
| --- | --- |
| ![알림](https://user-images.githubusercontent.com/61930524/216861716-68edba8e-f2a4-4ab6-a002-104dc0e29452.gif) | ![팔로잉](https://user-images.githubusercontent.com/61930524/216861703-2ddbe0f3-5284-41bd-91b5-881cf1f95dc8.gif) |

| 탐색 탭 | 검색 |
| --- | --- |
| ![탐색탭](https://user-images.githubusercontent.com/61930524/216841842-08fc1bfb-4afc-40f9-9909-394c61887408.gif) | ![검색및태그](https://user-images.githubusercontent.com/61930524/216841849-b91e986b-3000-426f-ba26-44afa08c25cf.gif) |

| 댓글 | 게시물 수정 |
| --- | --- |
| ![댓글](https://user-images.githubusercontent.com/61930524/216860499-dbf80078-f241-4e6c-a976-3e3c5eaa6dce.gif) | ![수정](https://user-images.githubusercontent.com/61930524/216860537-8a86f159-ea9e-4742-b162-ddd00f5f56c7.gif)
 |
<br>

### ⚙ ERD
<div align="center">
<img src="https://user-images.githubusercontent.com/61930524/216848584-6b833c1f-77c4-4391-8f47-979ad3159820.png" alt="ERD" width="85%" height="85%" />
</div>
<br>

### 💻 개발환경
```
- IntelliJ IDEA Ultimate 2022.3
- Java 17
- Spring Boot 2.7.2
- Gradle 7.5.1
- MySQL 8.0.29
```
<br>

### 📑 Trouble Shooting
- [MissingServletRequestPartException: 파일 업로드 구현 중 발생](https://yooniversal.github.io/project/post192/)
- [Query specified join fetching, but the owner of the fetched association was not present in the select list](https://yooniversal.github.io/project/post193/)
- [Could not resolve all files for configuration ':runtimeClasspath' (com.mysql:mysql-connector-j)](https://yooniversal.github.io/project/post194/)
- [The dependency of some of the beans in the application context form a cycle: 순환 참조 에러](https://yooniversal.github.io/project/post195/)
- [Could not initialize proxy - no Session: 영속성 컨텍스트](https://yooniversal.github.io/project/post196/)
<br>
