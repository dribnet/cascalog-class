(defproject cascalog-wordcount "1.0.0-SNAPSHOT"
  :description "Wordcount: Cascalog translation for hadoop's rosetta stone."
  :dependencies [
    [org.clojure/clojure "1.5.1"]
    [cascalog/cascalog-core "2.0.0-SNAPSHOT"]
  ]
  :plugins [[lein-midje "3.0.0"]]
  :profiles {
    :provided {
      :dependencies [
        [org.apache.hadoop/hadoop-core "1.0.3"]
        [cascalog/midje-cascalog "2.0.0-SNAPSHOT"]
      ]
    }
  }
  :aot [wordcount.simple wordcount.debug wordcount.counter]
  :uberjar-name "wordcount.jar"
  :min-lein-version "2.0.0"
)
