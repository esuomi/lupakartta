(defproject lupakartta/api "0.1.0-SNAPSHOT"
  :description "API/backend for Lupakartta"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[http-kit "2.3.0"]                        ; our http server of choice
                 [org.clojure/clojure "1.9.0"]             ; Clojure 1.9 itself (includes spec)
                 [org.clojure/tools.cli "0.3.7"]           ; Commandline interface parsing
                 [lupakartta/messenger  "0.1.0-SNAPSHOT"]  ; internal library for microservice communication
                 [lupakartta/middleware "0.1.0-SNAPSHOT"]  ; common Ring middleware
                 [metosin/reitit "0.2.3"]                  ; reitit is used for routing
                 [mount "0.1.13"]                          ; lots of people are a fan of component, I like mount for IoC
                 [ring/ring-core "1.7.0"]                  ; some readymade useful middleware, also required by sente
                ]
  :plugins [[lein-cljfmt "0.5.7"]       ; Clojure formatter/linter
            [lein-eftest "0.4.3"]       ; Better test runner for clojure.test
            [lein-kibit "0.1.5"]        ; There's a function for that - suggests alternates for better code
            [venantius/yagni "0.1.4"]   ; You Ain't Gonna Need it - dead code scanning
            [lein-nvd "0.5.0"]          ; Dependency security scanner
            [lein-ancient "0.6.15"]     ; Dependency version scanner
            [lein-cloverage "1.0.10"]]  ; Clojure test coverage

  :aot :all
  :main ^:skip-aot api.cli
  :target-path "target/%s"

  ;; Maven like directory structure, mainly to keep things clear and familiar for JVM developers
  :source-paths   ["src/main/clj"]
  :test-paths     ["src/test/clj"]
  :resource-paths ["src/main/resources"]

  :repl-options {:init-ns lupakartta.user}
  :eftest {:report       api.reporter/report
           :multithread? true}
  :profiles {:uberjar {:aot :all}
             :dev {:resource-paths ["src/test/resources"]
                   :dependencies [[eftest "0.5.2"]]}
             :test {:dependencies [[eftest "0.5.2"]]}})
