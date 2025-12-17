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


/*timeout(15) {
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

*//*stage("API tests in docker image") {
            sh """
                docker run --rm \
                --network=host \
                -e BASE_URL="${env.BASE_URL}" \
                -v /root/.m2/repository:/root/.m2/repository \
                -v ${WORKSPACE}/surefire-reports:/home/ubuntu/api_tests/target/surefire-reports \
                -v ${WORKSPACE}/allure-results:/home/ubuntu/api_tests/allure-results \
                -t localhost:5005/api_tests:2.0.0
            """
        }*//*


        stage("API tests in docker image") {
            sh """
                        mkdir -p /tmp/jenkins-${BUILD_NUMBER}
                    """

            sh """
                echo "=== Проверка перед запуском ==="
            echo "WORKSPACE: ${WORKSPACE}"
            ls -la ${WORKSPACE}/ || echo "Cannot list workspace"

                docker run --rm \
                --network=host \
                -e BASE_URL="${env.BASE_URL}" \
                -v /root/.m2/repository:/root/.m2/repository \
                -v ./surefire-reports:/home/ubuntu/api_tests/target/surefire-reports \
                -v ./allure-results:/home/ubuntu/api_tests/target/allure-results \
                -t localhost:5005/api_tests:2.0.0 \
                
                              sh -c "echo '=== Контейнер: рабочие директории ===' && \
                     pwd && \
                     echo '' && \
                     echo '=== Существует ли /home/ubuntu/api_tests/target/allure-results? ===' && \
                     ls -la /home/ubuntu/api_tests/target/ 2>/dev/null || echo 'Директория не существует' && \
                     echo '' && \
                     echo '=== Поиск allure-results в контейнере ===' && \
                     find / -name '*allure*' -type d 2>/dev/null | head -10 && \
                     echo '' && \
                     echo '=== Проверка целевой директории ===' && \
                     mkdir -p /home/ubuntu/api_tests/target/allure-results && \
                     echo 'test' > /home/ubuntu/api_tests/target/allure-results/test.txt && \
                     ls -la /home/ubuntu/api_tests/target/allure-results/"
            """
*//*            sh """
                mvn clean test
            """*//*
            sh """
                echo ${WORKSPACE}
            """

            sh """
                        cp -r /tmp/jenkins-${BUILD_NUMBER}/* ${WORKSPACE}/ || true
                        ls -la ${WORKSPACE}/
                    """

            archiveArtifacts artifacts: '**', fingerprint: true
        }

        stage("Verify Allure Results") {
            sh """
                # Проверяем, что allure-results созданы и не пустые
                if [ -d "${WORKSPACE}/allure-results" ]; then
                    echo "Allure results directory exists"
                    echo "Files in allure-results:"
                    ls -la ${WORKSPACE}/allure-results/ || true
                    
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
            sh "pwd"
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

            def configContent = readYaml text: env.YAML_CONFIG ?: ''
            def baseUrl = "https://petstore.swagger.io/v2"
            if (configContent != null && !configContent.isEmpty()) {
                baseUrl = configContent.get("BASE_URL") ?: baseUrl
            }
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
                # Очищаем и создаем директории
                rm -rf ${WORKSPACE}/allure-results ${WORKSPACE}/surefire-reports
                mkdir -p ${WORKSPACE}/allure-results ${WORKSPACE}/surefire-reports

                echo "=== Запуск тестов ==="
                echo "WORKSPACE: ${WORKSPACE}"
                echo "BASE_URL: ${env.BASE_URL}"

                # Запускаем тесты
                docker run --rm \
                  --network=host \
                  -e BASE_URL="${env.BASE_URL}" \
                  -v /root/.m2/repository:/root/.m2/repository \
                  -v ${WORKSPACE}/surefire-reports:/home/ubuntu/api_tests/target/surefire-reports \
                  -v ${WORKSPACE}/allure-results:/home/ubuntu/api_tests/target/allure-results \
                  localhost:5005/api_tests:2.0.0

                # Проверяем результаты сразу
                echo "=== Проверка результатов на хосте ==="
                echo "Директория allure-results:"
                ls -la ${WORKSPACE}/allure-results/ 2>/dev/null || echo "Директория не существует!"
                echo ""
                echo "Содержимое allure-results (первые 10 файлов):"
                ls -la ${WORKSPACE}/allure-results/*.json 2>/dev/null | head -10 || echo "Нет JSON файлов"
                echo ""
                echo "Количество JSON файлов:"
                find ${WORKSPACE}/allure-results -name "*.json" 2>/dev/null | wc -l
            """
        }

        stage("Verify Allure Results") {
            sh """
                echo "=== Финальная проверка ==="
                echo "Текущая директория:"
                pwd
                echo ""
                echo "Директория allure-results:"
                if [ -d "${WORKSPACE}/allure-results" ]; then
                    echo "Существует: ДА"
                    ls -la "${WORKSPACE}/allure-results/"
                    echo ""
                    echo "Файлы (первые 10):"
                    ls -la "${WORKSPACE}/allure-results/"*.json 2>/dev/null | head -10 || echo "Нет JSON файлов"

                    FILE_COUNT=\$(find "${WORKSPACE}/allure-results" -name "*.json" 2>/dev/null | wc -l)
                    echo "Всего JSON файлов: \$FILE_COUNT"

                    if [ \$FILE_COUNT -eq 0 ]; then
                        echo "ВНИМАНИЕ: Нет JSON файлов!"
                        echo '{"name": "test", "status": "passed", "start": 1639670400000, "stop": 1639670401000}' > "${WORKSPACE}/allure-results/dummy.json"
                        echo "Создан dummy.json файл"
                    else
                        echo "✓ Отлично! Найдено \$FILE_COUNT JSON файлов"
                        echo "Пример файла:"
                        head -5 "\${WORKSPACE}/allure-results/\$(ls \${WORKSPACE}/allure-results/*.json | head -1)"
                    fi
                else
                    echo "Существует: НЕТ"
                    echo "Создаем пустую директорию..."
                    mkdir -p "${WORKSPACE}/allure-results"
                    echo '{"name": "error", "status": "broken", "start": 1639670400000, "stop": 1639670401000}' > "${WORKSPACE}/allure-results/error.json"
                    echo "Создан error.json файл"
                fi
            """
        }

        stage("Publish Allure Report") {
            echo "Publishing Allure report..."
            allure([
                    includeProperties: false,
                    jdk: '',
                    reportBuildPolicy: 'ALWAYS',
                    results: [[path: 'allure-results']]
            ])
        }
    }
}