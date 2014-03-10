(ns leiningen.coffee
  (:require [leiningen.deps :as deps]
            [leiningen.shell :as shell]))

(def default-coffee-bin "node_modules/coffee-script/bin/coffee")

(defn build-default-cmd
  [{:keys [bin bare compile join output watch sources],
    :as opts,
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

(defn build-default-cmds [{:keys [invocations], :or {invocations []}, :as opts}]
  (if (empty? invocations)
    [(build-default-cmd (dissoc opts :invocations))]
    (->> (map (fn [inv] (merge (dissoc opts :invocations) inv)) invocations)
         (map build-default-cmd))))

(defn build-watch-cmds
  [cfg]
  [(-> cfg (merge {:watch true}) build-default-cmd)])

(defn build-run-cmds
  [{:keys [bin], :or {bin default-coffee-bin}} args]
  [(concat [bin] args)])

(defn coffee
  [project & args]
  (let [opts (get-in project [:lein-coffee :coffee] {})
        cmds (case (first args)
              "watch" (build-watch-cmds opts)
              "run" (build-run-cmds opts (rest args))
              (build-default-cmds opts))]
    (doseq [cmd cmds]
      (println "lein-coffee: shell:" cmd)
      (apply shell/shell cmd))))
