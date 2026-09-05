pipeline {

    agent any

    stages {

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

    }
}