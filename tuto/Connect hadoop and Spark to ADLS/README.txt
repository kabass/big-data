
Coté azure
 1) Créer un service pricipal
 2) Assigner le role "Storage Blob Data Contributor" ou donner lui la permission directement dans le container.

Connection hadoop à ADLS GEN2
 1) télécharger hadoop
 2) Suivre le https://docs.cloudera.com/documentation/enterprise/6/6.3/topics/admin_adls2_config.html
   2.1)  Core site 
        <property>
        <name>fs.azure.account.auth.type</name>
        <value>OAuth</value>
      </property>
   <property>
      <name>fs.azure.account.oauth.provider.type</name>
      <value>org.apache.hadoop.fs.azurebfs.oauth2.ClientCredsTokenProvider</value>
  </property>  
  
  <property>
      <name>fs.azure.account.oauth2.client.endpoint</name>
      <value>https://login.microsoftonline.com/d1281961-ab47-4e5c-9977-ef638848789a/oauth2/token</value>
  </property>
  <property>
      <name>fs.azure.account.oauth2.client.id</name>
      <value>4ffa2f78-adde-44ab-8c8b-0f6b820b4a99</value>
  </property>
  <property>
      <name>fs.azure.account.oauth2.client.secret</name>
      <value>SECRET</value>
  </property>

 2.2) Configuring Native TLS Acceleration
    - Trouver le chemin de l'installation de openssl avec "whereis libssl"
    - Ajouter dans hadoop-env.sh export HADOOP_OPTS="-Dorg.wildfly.openssl.path=[CHEMIN OPEN SSL ] ${HADOOP_OPTS}"
 2.3) Mettre à jour hadoop-env.sh
    -  export HADOOP_CLASSPATH="[HADOOP-HOME]/share/hadoop/tools/lib/*"
    -  export JAVA_HOME=[JAVA HOME]
 
Connection spark à ADLS en passant par hadoop
 1) télécharger spark sans hadoop
 2) assigner la variable d'environnement SPARK_DIST_CLASSPATH à la valeur de "hadoop classpath"

Connection spark à ADLS sans passer par HADOOP
avec oauth2
spark.conf.set("fs.azure.account.auth.type.bkaadls.dfs.core.windows.net", "OAuth")
spark.conf.set("fs.azure.account.oauth.provider.type.bkaadls.dfs.core.windows.net", "org.apache.hadoop.fs.azurebfs.oauth2.ClientCredsTokenProvider")
spark.conf.set("fs.azure.account.oauth2.client.id.bkaadls.dfs.core.windows.net", "4ffa2f78-adde-44ab-8c8b-0f6b820b4a99")
spark.conf.set("fs.azure.account.oauth2.client.secret.bkaadls.dfs.core.windows.net", "[secret]")
spark.conf.set("fs.azure.account.oauth2.client.endpoint.bkaadls.dfs.core.windows.net", "https://login.microsoftonline.com/d1281961-ab47-4e5c-9977-ef638848789a/oauth2/token")

avec l'access key
spark.conf.set(
  "fs.azure.account.key.bkaadls.dfs.core.windows.net","ACCESS-KEY")

Test :
hdfs dfs -ls abfss://container-1@bkaadls.dfs.core.windows.net/")
spark.read.csv("abfss://container-1@bkaadls.dfs.core.windows.net/aws")