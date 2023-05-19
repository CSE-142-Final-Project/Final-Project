echo "Starting Gradle"
echo
chmod +x gradlew

echo "running unit tests"
./gradlew test
echo "unit tests finished, starting"
./gradlew run