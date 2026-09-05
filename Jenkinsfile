pipeline {
    agent any

    stages {

        stage('Environment Test') {
            steps {
                sh '''
                    echo "Job Name: $JOB_NAME"
                    echo "Build Number: $BUILD_NUMBER"
                    echo "Workspace: $WORKSPACE"
                    echo "Build URL: $BUILD_URL"
                '''
            }
        }

        stage('Backend Build') {
            steps {
                dir('backend/hrms') {
                    sh '''
                        export JAVA_HOME="/opt/homebrew/Cellar/openjdk@21/21.0.12.1/libexec/openjdk.jdk/Contents/Home"
                        export PATH="$JAVA_HOME/bin:/opt/homebrew/bin:/usr/local/bin:$PATH"

                        echo "Java:"
                        java -version

                        echo "Maven:"
                        mvn -version

                        echo "Building backend..."
                        mvn clean package
                    '''
                }
            }
        }

        stage('Frontend Build') {
            steps {
                dir('frontend/hrms-ui') {
                    sh '''
                        export PATH="/opt/homebrew/bin:$PATH"

                        echo "Node:"
                        node -v

                        echo "NPM:"
                        npm -v

                        echo "Installing dependencies..."
                        npm install

                        echo "Building frontend..."
                        npm run build
                    '''
                }
            }
        }

        stage('Docker Build') {
            steps {
                sh '''
                    export PATH="/usr/local/bin:/opt/homebrew/bin:$PATH"

                    echo "Docker:"
                    docker --version

                    echo "Building backend image..."
                    docker build -t hrms-backend:${BUILD_NUMBER} backend/hrms

                    echo "Building frontend image..."
                    docker build -t hrms-frontend:${BUILD_NUMBER} frontend/hrms-ui

                    echo "Docker images:"
                    docker images | grep -E "hrms-backend|hrms-frontend"
                '''
            }
        }
    }
}