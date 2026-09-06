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

                    docker build \
                        -t hrms-backend:${BUILD_NUMBER} \
                        -t hrms-backend:latest \
                        backend/hrms

                    echo "Building frontend image..."

                    docker build \
                        -t hrms-frontend:${BUILD_NUMBER} \
                        -t hrms-frontend:latest \
                        frontend/hrms-ui

                    echo "Docker images:"

                    docker images | grep -E "hrms-backend|hrms-frontend"
                '''
            }
        }

        stage('Docker Push') {
            steps {
                withCredentials([
                    usernamePassword(
                        credentialsId: 'dockerhub-credentials',
                        usernameVariable: 'DOCKER_USERNAME',
                        passwordVariable: 'DOCKER_PASSWORD'
                    )
                ]) {
                    sh '''
                        export PATH="/usr/local/bin:/opt/homebrew/bin:$PATH"

                        echo "Logging into Docker Hub..."

                        echo "$DOCKER_PASSWORD" | docker login \
                            -u "$DOCKER_USERNAME" \
                            --password-stdin

                        echo "Tagging backend images..."

                        docker tag hrms-backend:${BUILD_NUMBER} \
                            $DOCKER_USERNAME/hrms-backend:${BUILD_NUMBER}

                        docker tag hrms-backend:latest \
                            $DOCKER_USERNAME/hrms-backend:latest

                        echo "Pushing backend images..."

                        docker push \
                            $DOCKER_USERNAME/hrms-backend:${BUILD_NUMBER}

                        docker push \
                            $DOCKER_USERNAME/hrms-backend:latest


                        echo "Tagging frontend images..."

                        docker tag hrms-frontend:${BUILD_NUMBER} \
                            $DOCKER_USERNAME/hrms-frontend:${BUILD_NUMBER}

                        docker tag hrms-frontend:latest \
                            $DOCKER_USERNAME/hrms-frontend:latest

                        echo "Pushing frontend images..."

                        docker push \
                            $DOCKER_USERNAME/hrms-frontend:${BUILD_NUMBER}

                        docker push \
                            $DOCKER_USERNAME/hrms-frontend:latest


                        echo "Docker images pushed successfully!"
                    '''
                }
            }
        }
        stage('Deploy') {
            steps {
                sh '''
                    export PATH="/usr/local/bin:/opt/homebrew/bin:$PATH"

                    echo "Starting deployment..."

                    docker compose pull

                    docker compose up -d

                    echo "Checking containers..."

                    docker compose ps

                    echo "Deployment completed successfully!"
                '''
            }
        }



    }
}// GitHub webhook test
