(ns url-shortener.url
  (:require [taoensso.carmine :as car :refer [wcar]]))

(def server1-conn {:pool {} :spec {:uri (System/getenv "REDISTOGO_URL")}})

(defmacro wcar* [& body] `(car/wcar server1-conn ~@body))

(defn all [] (wcar* (car/hkeys "url")))

(defn fetch [id] (wcar* (car/hget "url" id)))

(defn shorten!
  "Stores the given URL under a new identifier, or the given identifier
   if provided. Return the identifier as a string.
   Modifies the global mapping accordingly."
  ([url]
     (let [id (wcar* (car/incr "counter"))
           id (Long/toString id 36)]
       (or (shorten! url id)
           (recur url))))
  ([url id]
     (dosync
      (when-not (fetch id)
        (wcar* (car/hsetnx "url" id url))
        id))))
