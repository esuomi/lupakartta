(defproject lupakartta/ui "0.1.0-SNAPSHOT"
  :dependencies [[com.taoensso/sente "1.13.1"]           ; WebSocket communication
                 [garden "1.3.6"]                        ; Generate CSS with Clojure
                 [org.clojure/clojure "1.9.0"]
                 [org.clojure/clojurescript "1.10.238"]  ;
                 [reagent "0.7.0"]
                 [re-frame "0.10.5"]]

  :plugins [[lein-cljsbuild "1.1.7"]
            [lein-garden "0.3.0"]]

  :min-lein-version "2.5.3"

  :source-paths ["src/clj" "src/cljs"]

  :clean-targets ^{:protect false} ["resources/public/js/compiled"
                                    "target"
                                    "resources/public/css"]

  :figwheel {:css-dirs ["resources/public/css"]}

  :garden {:builds [{:id           "screen"
                     :source-paths ["src/clj"]
                     :stylesheet   ui.css/screen
                     :compiler     {:output-to     "resources/public/css/screen.css"
                                    :pretty-print? true}}]}

  :profiles {:dev  {:dependencies [[binaryage/devtools "0.9.10"]
                                   [day8.re-frame/re-frame-10x "0.3.3"]
                                   [day8.re-frame/tracing "0.5.1"]]
                    :plugins      [[lein-figwheel "0.5.16"]]}
             :prod { :dependencies [[day8.re-frame/tracing-stubs "0.5.1"]]}}

  :cljsbuild
  {:builds
   [{:id           "dev"
     :source-paths ["src/cljs"]
     :figwheel     {:on-jsload "ui.core/mount-root"}
     :compiler     {:main                 ui.core
                    :output-to            "resources/public/js/compiled/app.js"
                    :output-dir           "resources/public/js/compiled/out"
                    :asset-path           "js/compiled/out"
                    :source-map-timestamp true
                    :preloads             [devtools.preload
                                           day8.re-frame-10x.preload]
                    :closure-defines      {"re_frame.trace.trace_enabled_QMARK_"        true
                                           "day8.re_frame.tracing.trace_enabled_QMARK_" true}
                    :external-config      {:devtools/config {:features-to-install :all}}
                    }}

    {:id           "min"
     :source-paths ["src/cljs"]
     :compiler     {:main            ui.core
                    :output-to       "resources/public/js/compiled/app.js"
                    :optimizations   :advanced
                    :closure-defines {goog.DEBUG false}
                    :pretty-print    false}}


    ]}
  )
