(ns url-shortener.core
  (:require [ring.adapter.jetty :refer [run-jetty]]
            [ring.middleware.params :refer [wrap-params]]
            [ring.util.response]
            [compojure.core :refer [GET PUT POST defroutes]]
            [compojure handler route])
  (:gen-class))

(def ^:private counter (atom 0))

(def ^:private mappings (ref {}))

(defn url-for [id] (@mappings id))

(defn shorten!
  "Stores the given URL under a new identifier, or the given identifier
   if provided. Return the identifier as a string.
   Modifies the global mapping accordingly."
  ([url]
     (let [id (swap! counter inc)
           id (Long/toString id 36)]
       (or (shorten! url id)
           (recur url))))
  ([url id]
     (dosync
      (when-not (@mappings id)
        (alter mappings assoc id url)
        id))))

(defn retain
  [& [url id :as args]]
  (if-let [id (apply shorten! args)]
    {:status 201
     :headers {"Location" id}
     :body (list "URL " url " assigned the short identifier " id)}
    {:status 409
     :body (format "Short URL %s is already taken" id)}))

(defn redirect
  [id]
  (if-let [url (url-for id)]
    (ring.util.response/redirect url)
    {:status 404 :body (str "No such short URL: " id)}))

(defroutes app*
  (GET "/" request "Welcome!")
  (PUT "/:id" [id url] (retain url id))
  (POST "/" [url] (retain url))
  (GET "/:id" [id] (redirect id))
  (GET "/list/" [] (interpose "\n" (keys @mappings)))
  (compojure.route/not-found "Sorry, there's nothing here."))

(def app (compojure.handler/api app*))

(defn -main
  [& args]
  (let [port (Integer. (or (first args) 8080))]
    (run-jetty app {:port port :join? false})))
