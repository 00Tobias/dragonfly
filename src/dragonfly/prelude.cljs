(ns dragonfly.prelude
  (:require
   ["three/build/three.module.js" :as three]))

(defn init-prelude []
  (def ^js container (.getElementById js/document "dragonfly"))
  (def ^js scene (three/Scene.)))
