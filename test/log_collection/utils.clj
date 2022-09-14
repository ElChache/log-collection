(ns log-collection.utils
  (:require [clojure.test :refer :all]
            [clojure.java.io :as io]))

(defn generate-log-file-in-memory
  "Writes the given text N times in a file in separate lines.
   Writes the output under the given output file.
   Generates all the string in memory and dumps it into the log file once."
  [file-output line n-times]

  (let [file-res (io/file file-output)]
    (spit file-res "")
    (spit file-res
          (reduce (fn [res n]
                    (str res (str n " - " line "\n")))
                  ""
                  (range n-times)))))

(defn generate-log-file
  "Writes the given text N times in a file in separate liens.
   Writes the output under the given output file.
   Writes into the file once per line"
  [file-output line n-times]

  (let [file-res (io/file file-output)]
    (spit file-res "")
    (doseq [n-text (range n-times)]
      (spit file-res (str n-text " - " line "\n") :append true))))