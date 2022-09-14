(ns log-collection.server
  (:require [compojure.core :refer [defroutes GET]]
            [compojure.route :as route]
            [compojure.coercions :as coercions]
            [ring.adapter.jetty :as jetty]
            [ring.middleware.cors :refer [wrap-cors]]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
            [ring.middleware.json :refer [wrap-json-response]]
            [ring.middleware.reload :as reload]
            [log-collection.handlers :as handlers]))

;; The server routes
(defroutes
  routes
  (GET "/lines" [filename lines :<< coercions/as-int keyword]
    (handlers/lines-handler filename lines keyword))

  (route/not-found "Not found"))

(defn start [config]
  (println ";; Starting server with configuration:" config "...")
  (let [{:keys [port join? dev?]} config
        app (-> routes

                ;; Default settings for an api
                (wrap-defaults api-defaults)

                ;; Responses are given in json format
                wrap-json-response

                ;; Setting up CORS so it works from a frontend web application
                (wrap-cors :access-control-allow-origin (fn cb [req] true)
                           :access-control-allow-methods [:get :post :patch :put :delete]))]
    (jetty/run-jetty
      (if dev? (reload/wrap-reload app) app)
      {:port port :join? join?})))