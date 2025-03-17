(ns dragonfly.camera
  (:require
   [dragonfly.prelude :refer [container]]
   ["three/build/three.module.js" :as three]))

(defn init-camera []
  (def ^js camera (three/PerspectiveCamera. 90
                                            (/ (.-clientWidth  container)
                                               (.-clientHeight container))
                                            1
                                            5000))
  (set! (.. camera -position -z) 5))
