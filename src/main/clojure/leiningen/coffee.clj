(ns leiningen.coffee
  (:import [java.nio.file FileSystems Path Paths StandardWatchEventKinds])
  (:require [clojure.pprint :refer [pprint]]
            [leiningen.deps :as deps]
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


;; From swank-clojure swank.util.string
(defn largest-common-prefix
  "Returns the largest common prefix of two strings."
  ([#^String a, #^String b]
     (apply str (take-while (comp not nil?) (map #(when (= %1 %2) %1) a b))))
  {:tag String})

(defn start-watching
  [opts]
  ;[(-> opts (merge {:watch true}) build-default-cmd)]
  ;(let [watcher (if (= (clojure.string/match #"Mac OS X" (System/getProperty "os.name")))
  ;                (com.barbarysoftware.watchservice.WatchService/newWatchService)
  ;                (.newWatchService (FileSystems/getDefault)))]
  ;(println opts)
  (pprint (-> opts
              :invocations
              (->> (map :sources)
                   (map #(reduce largest-common-prefix %)))))
  (System/exit 100))

(defn build-run-cmd
  [{:keys [bin], :or {bin default-coffee-bin}} args]
  (concat [bin] args))

(defn- run-commands [& cmds]
  (doseq [cmd cmds]
    (println "lein-coffee: shell:" cmd)
    (apply shell/shell cmd)))

(defn coffee
  [project & args]
  (let [opts (get-in project [:lein-coffee :coffee] {})]
    (case (first args)
      "watch" (start-watching opts)
      "run" (run-commands (build-run-cmd opts (rest args)))
      (apply run-commands (build-default-cmds opts)))))
