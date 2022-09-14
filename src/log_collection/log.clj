(ns log-collection.log
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

(defn line-matches?
  "Very basic matching based on existence of the given keyword.
  Returns true if the match is empty"
  [match line]
  (some? (str/index-of (str/lower-case line)
                       (str/lower-case (or match "")))))

(defn lines*
  "Returns up to `n` lines that contain the keyword in `match`.
  It is important to note that it will not execute the filter on every line of the file,
  but only executes the filter until the sequence has `n` values. That is because `line-seq` returns
  a lazy sequence, so the filter is not evaluated immediately.
  This makes this function perform well even with very long files."
  [file-reader n match]
  (->> (line-seq file-reader)
       (filter (partial line-matches? match))
       (take n)))

(defn lines
  "The given file must exist in the /var/log folder"
  [file n match]
  (with-open [file-reader (io/reader file)]

    ;; doall is used here to force it to evaluate the returned lazy sequence before it closes the file
    (doall (lines* file-reader n match))))

(defn exists? [file]
  (.exists (io/file file)))