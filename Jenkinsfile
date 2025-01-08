pipeline {
    agent any

    environment {
        CHROME_DRIVER_PATH = '/path/to/chromedriver'  // ChromeDriver 경로
    }

    stages {
        stage('Checkout Code') {
            steps {
                // Git 리포지토리에서 코드를 체크아웃
                git branch: 'main', url: 'https://github.com/your-repo/shopping-mall.git'
            }
        }

        stage('Build') {
            steps {
                // Maven 빌드 (또는 Gradle 빌드)
                sh './mvnw clean install'  // Maven
                // sh './gradlew build'  // Gradle
            }
        }

        stage('Run Selenium Tests') {
            steps {
                // Selenium 테스트 실행
                sh './mvnw test'  // Maven으로 테스트 실행
                // sh './gradlew test'  // Gradle로 테스트 실행
            }
        }
    }

    post {
        always {
            // 테스트 결과 아카이브
            archiveArtifacts artifacts: 'target/test-classes/*.xml', allowEmptyArchive: true
        }
        success {
            echo '테스트 성공!'
        }
        failure {
            echo '테스트 실패...'
        }
    }
}