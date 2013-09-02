(ns url-shortener.core
  (:require [url-shortener.url :as url]
            [ring.adapter.jetty :refer [run-jetty]]
            [ring.middleware.params :refer [wrap-params]]
            [ring.util.response]
            [ring.middleware.json :refer [wrap-json-response]]
            [compojure.core :refer [GET PUT POST defroutes]]
            [compojure handler route])
  (:gen-class))

(defn retain
  [& [url id :as args]]
  (if-let [id (apply url/shorten! args)]
    {:status 201
     :headers {"Location" id}
     :body {:url url :id id}}
    {:status 409
     :body {:error (format "Short URL %s is already taken" id)}}))

(defn redirect
  [id]
  (if-let [url (url/fetch id)]
    (ring.util.response/redirect url)
    {:status 404
     :body {:error (format "No such short URL %s" id)}}))

(defroutes app*
  (GET "/" [] (keys (url/all)))
  (PUT "/:id" [id url] (retain url id))
  (POST "/" [url] (retain url))
  (GET "/:id" [id] (redirect id))
  (compojure.route/not-found {:body {:error "Sorry, there's nothing here."}}))

(def app
  (-> app*
      wrap-json-response
      compojure.handler/api))

(defn -main
  [& args]
  (let [port (Integer. (or (first args) 8080))]
    (run-jetty app {:port port :join? false})))
