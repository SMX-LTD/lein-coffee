(ns lein-coffee.plugin
  (:require [lein-npm.plugin :as npm]))

(def default-coffee-version ">=1.6")

(defn hooks []
  (npm/hooks))

(defn- coffee-script?
  [dep]
  (= (str (first dep)) "coffee-script"))

(defn- find-coffee-deps
  [deps]
  (keep-indexed #(when (coffee-script? %2) %1) deps))

(defn- ensure-coffee
  ([deps version]
     (let [coffee-matches (find-coffee-deps deps)
           new-coffee-dep ["coffee-script" (or version default-coffee-version)]]
       (println (format "Adding npm dependency on %s" new-coffee-dep))
       (if (empty? coffee-matches)
         (conj deps new-coffee-dep)
         (assoc-in deps (first coffee-matches) new-coffee-dep))))
  ([deps]
     (ensure-coffee deps default-coffee-version)))

(defn middleware
  [project]
  (update-in project [:node-dependencies] #(vec (ensure-coffee % (get-in project [:coffee :version])))))
