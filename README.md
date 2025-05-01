Still WIP
## Compiling

1. Clone repository.
   ```
   git clone -b master --single-branch https://github.com/stabu-dev/MindustryModTemplate
   ```

2. Pack sprites. (Only necessary if new sprites are added)
   ```
   gradlew tools:proc
   ```

3. Build.
   ```
   gradlew main:deploy
   ```

Resulting `.jar` file should be in `main/build/libs/`
