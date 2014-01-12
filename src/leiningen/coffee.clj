(ns leiningen.coffee
  (:require [robert.hooke]
            [leiningen.deps :as deps]
            [leiningen.shell :as shell]))

(def default-coffee-bin "node_modules/coffee-script/bin/coffee")

(defn coffee-cmd
  [{:keys [coffee-bin
           coffee-bare
           coffee-compile
           coffee-join
           coffee-output
           coffee-watch
           coffee-sources],
    :or {coffee-bin default-coffee-bin
         coffee-compile true
         coffee-sources []}}]
  (let [flags [(and coffee-watch "-w")
               (and coffee-compile "-c")
               (and coffee-bare "-b")
               (and coffee-join ["-j" coffee-join])
               (and coffee-output ["-o" coffee-output])]]
    (as-> flags f
          (filter (complement nil?) f)
          (flatten f)
          (concat [coffee-bin] f coffee-sources))))

(defn coffee-watch
  [cfg]
  (-> cfg (merge {:coffee-watch true}) coffee-cmd))

(defn coffee-run
  [{:keys [coffee-bin], :or {coffee-bin default-coffee-bin}} args]
  (concat [coffee-bin] args))

(defn coffee
  [project & args]
  (let [opts (select-keys project [:coffee-bare :coffee-output :coffee-join :coffee-sources])
        cmd (case (first args)
              "watch" (coffee-watch opts)
              "run" (coffee-run opts (rest args))
              (coffee-cmd opts))]
    (apply shell/shell cmd)))
