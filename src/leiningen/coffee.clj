(ns leiningen.coffee
  (:require [leiningen.deps :as deps]
            [leiningen.shell :as shell]))

(def default-coffee-bin "node_modules/coffee-script/bin/coffee")

(defn build-default-cmd
  [{:keys [bin bare compile join output watch sources],
    :or {bin default-coffee-bin
         compile true
         sources []}}]
  (let [flags [(and watch "-w")
               (and compile "-c")
               (and bare "-b")
               (and join ["-j" join])
               (and output ["-o" output])]]
    (as-> flags f
          (remove nil? f)
          (flatten f)
          (concat [bin] f sources))))

(defn build-watch-cmd
  [cfg]
  (-> cfg (merge {:watch true}) build-default-cmd))

(defn build-run-cmd
  [{:keys [bin], :or {bin default-coffee-bin}} args]
  (concat [bin] args))

(defn coffee
  [project & args]
  (let [opts (get-in project [:lein-coffee :coffee] {})
        cmd (case (first args)
              "watch" (build-watch-cmd opts)
              "run" (build-run-cmd opts (rest args))
              (build-default-cmd opts))]
    (apply shell/shell cmd)))
