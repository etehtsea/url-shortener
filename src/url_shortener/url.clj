(ns url-shortener.url)

(def ^:private counter (atom 0))

(def ^:private mappings (ref {}))

(defn all [] @mappings)

(defn fetch [id] (@mappings id))

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
      (when-not (fetch id)
        (alter mappings assoc id url)
        id))))
