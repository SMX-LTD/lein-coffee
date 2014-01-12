(ns lein-coffee.plugin
  (:require [lein-npm.plugin :as npm]))

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
           new-coffee-dep ["coffee-script" version]]
      (if (empty? coffee-matches)
        (conj deps new-coffee-dep)
        (assoc-in deps (first coffee-matches) new-coffee-dep))))
  ([deps]
     (ensure-coffee deps ">=1.6")))

(defn middleware
  [project]
  (update-in project [:node-dependencies] #(ensure-coffee % (:coffee-version project))))
