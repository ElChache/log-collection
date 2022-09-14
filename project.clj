(defproject log-collection "1.0"
  :description "API that provides logs from files"
  :url "https://github.com/ElChache/log-collection"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[clojure-interop/apache-commons-io "1.0.0"]
                 [compojure "1.7.0"]
                 [cprop "0.1.19"]
                 [org.clojure/clojure "1.10.3"]
                 [ring "1.9.5"]
                 [ring/ring-defaults "0.3.3"]
                 [ring/ring-json "0.5.1"]
                 [ring/ring-jetty-adapter "1.9.5"]
                 [ring-cors "0.1.13"]]
  :main ^:skip-aot log-collection.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}})
