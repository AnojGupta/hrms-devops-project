pipeline {

    agent any

    stages {

        stage('Backend Build') {
            steps {
                dir('backend/hrms') {
                    sh 'mvn clean package'
                }
            }
        }

    }
}