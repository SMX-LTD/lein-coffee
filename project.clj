(defproject lein-coffee "0.4.1"
  :description "Leiningen plugin thinly wrapping the CoffeeScript compiler"
  :url "https://github.com/SMX-LTD"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[lein-npm "0.4.0"]
                 [lein-shell "0.3.0"]]
  :eval-in-leiningen true

  :source-paths ["src/main/clojure" "tasks"])
