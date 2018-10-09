(defproject lupakartta/root "MONOLITH"
  :description "Lupakartta monorepo"

  :plugins  [[lein-monolith "1.1.0"]
             [lein-cprint "1.2.0"]]

  :dependencies [[org.clojure/clojure "1.9.0"]]

  :test-selectors {:unit        (complement :integration)
                   :integration :integration}

  :monolith {:inherit [:test-selectors :env]
             :inherit-leaky [:repositories :managed-dependencies]
             :project-selectors {:deployable :deployable :unstable #(= (first (:version %)) \0)}
             :project-dirs ["libs/*" "apps/*"]})

