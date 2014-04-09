(ns lein-coffee.plugin
  (:require [lein-npm.plugin :as npm]
            [robert.hooke :as hooke]
            leiningen.compile
            leiningen.jar
            leiningen.coffee))

(def default-coffee-version ">=1.6")

(defn compile-hook [task project & args]
  (apply task project args)
  (when (and (:lein-coffee project) (get-in project [:lein-coffee :compile-hook] true))
    (leiningen.coffee/coffee project)))

(defn jar-hook [task project & args]
  (when (and (:lein-coffee project) (get-in project [:lein-coffee :jar-hook] true))
    (leiningen.coffee/coffee project))
  (apply task project args))

(defn hooks []
  (npm/hooks)
  (hooke/add-hook #'leiningen.compile/compile #'compile-hook)
  (hooke/add-hook #'leiningen.jar/jar #'jar-hook))

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
       (if (empty? coffee-matches)
         (conj deps new-coffee-dep)
         (assoc-in deps (first coffee-matches) new-coffee-dep))))
  ([deps]
     (ensure-coffee deps default-coffee-version)))

(defn middleware
  [project]
  (update-in project [:node-dependencies] #(vec (ensure-coffee % (get-in project [:lein-coffee :coffee-version])))))
