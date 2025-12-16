/*
timeout(15) {
    node("maven") {
        wrap([$class: 'BuildUser']) {
            currentBuild.description = """
build user: ${BUILD_USER}
branch: ${REFSPEC}
"""

            config = readYaml text: env.YAML_CONFIG

            if (config != null) {
                for (param in config.entrySet()) {
                    env.setProperty(param.getKey(), param.getValue())
                }
            }
        }

        stage("Checkout") {
            checkout scm;
        }

        stage('Build Docker Image') {
            docker.build("localhost:5005/api_tests:2.0.0")
        }

        stage("Create configuration") {
            sh "echo BASE_URL=${env.getProperty('BASE_URL')} > ./.env"
        }

        stage("API tests in docker image") {
            sh "docker run --rm \
            --network=host --env-file ./.env \
            -v /root/.m2/repository:/root/.m2/repository \
            -v ./surefire-reports:/home/ubuntu/api_tests/target/surefire-reports \
            -v ./allure-results:/home/ubuntu/api_tests/target/allure-results \
            -t localhost:5005/api_tests:${env.getProperty('TEST_VERSION')}"
        }

        stage("Publish Allure Reports") {
            allure([
                    includeProperties: false,
                    jdk: '',
                    properties: [],
                    reportBuildPolicy: 'ALWAYS',
                    results: [[path: './allure-results']]
            ])
        }
    }
}*/
timeout(15) {
    node("maven") {
        wrap([$class: 'BuildUser']) {
            currentBuild.description = """
build user: ${BUILD_USER}
branch: ${REFSPEC}
"""

            // Читаем конфиг безопасным способом
            def configContent = readYaml text: env.YAML_CONFIG ?: ''
            def baseUrl = "https://petstore.swagger.io/v2" // значение по умолчанию

            if (configContent != null && !configContent.isEmpty()) {
                // Безопасное получение значения
                baseUrl = configContent.get("BASE_URL") ?: baseUrl
            }

            // Сохраняем в переменной
            env.BASE_URL = baseUrl
        }

        stage("Checkout") {
            checkout scm;
        }

        stage('Build Docker Image') {
            docker.build("localhost:5005/api_tests:2.0.0")
        }

        stage("API tests in docker image") {
            sh """
                docker run --rm \
                --network=host \
                -e BASE_URL="${env.BASE_URL}" \
                -v /root/.m2/repository:/root/.m2/repository \
                -v ./surefire-reports:/home/ubuntu/api_tests/target/surefire-reports \
                -v ./allure-results:/var/jenkins_home/workspace/api-tests/allure-results \
                -t localhost:5005/api_tests:2.0.0
            """
        }

        stage("Verify Allure Results") {
            sh """
                # Проверяем, что allure-results созданы и не пустые
                if [ -d "./allure-results" ]; then
                    echo "Allure results directory exists"
                    echo "Files in allure-results:"
                    ls -la ./allure-results/ || true
                    
                    # Подсчитываем количество файлов
                    FILE_COUNT=\$(find ./allure-results -type f | wc -l)
                    echo "Number of files in allure-results: \$FILE_COUNT"
                    
                    if [ \$FILE_COUNT -eq 0 ]; then
                        echo "WARNING: allure-results directory is empty!"
                        # Показываем содержимое target для диагностики
                        echo "Checking target directory structure..."
                        docker run --rm \
                          -v /root/.m2/repository:/root/.m2/repository \
                          localhost:5005/api_tests:2.0.0 \
                          ls -la /home/ubuntu/api_tests/target/ || true
                    fi
                else
                    echo "ERROR: allure-results directory not found!"
                fi
            """
        }

        stage("Publish Allure Reports") {
            allure([
                    includeProperties: false,
                    jdk: '',
                    properties: [],
                    reportBuildPolicy: 'ALWAYS',
                    results: [[path: './allure-results']]
            ])
        }
    }
}
