(ns log-collection.core
  (:require [cprop.core :as cprop]
            [log-collection.server :as server])
  (:gen-class))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (let [config (cprop/load-config)]
    (println "Hello, World!")
    (println "config;" config)
    (server/start (:server config))))
