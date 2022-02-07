# 使用手册

使用骨架生活成后需要修改，conf/setenv.sh 中MAIN_CLASS为实际包下。

> 通常使用idea工具，也可以直接使用命令。

```shell
mvn archetype:generate \
    -DarchetypeGroupId=com.seezoon \
    -DarchetypeArtifactId=seezoon-application-archetype \
    -DarchetypeVersion=1.0.0-SNAPSHOT \
    -DgroupId=${your ggroupId} \
    -DartifactId=${your artifactId} \
    -Dversion=1.0.0-SNAPSHOT \
    -DinteractiveMode=false
```