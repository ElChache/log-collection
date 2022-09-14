(ns log-collection.handlers
  (:require [clojure.string :as str]
            [ring.util.response :as response]
            [log-collection.log :as log]))

(defn lines-handler [filename lines keyword]
  (let [file (str "/var/log/" filename)]
    (cond

      ;; A filename must be given
      (str/blank? filename)
      (response/bad-request {:error "A filename must be given in the <filename> request parameter"})

      ;; The file inside the logs folder should exist
      ;; The response http code could also be NOT FOUND
      (not (log/exists? file))
      (response/bad-request {:error (str file " doesn't exist")})

      ;; `lines` needs to be a positive number (0 is not a positive number)
      (not (pos-int? lines))
      (response/bad-request {:error "The parameter <lines> needs to be a positive integer"})

      :else
      (response/response
        {:lines (log/lines file lines keyword)}))))