(ns dragonfly.main
  (:require
   [dragonfly.prelude :refer [init-prelude container]]
   [dragonfly.render :refer [init-render renderer composer]]
   [dragonfly.camera :refer [init-camera camera]]
   [dragonfly.world :refer [init-world update-world]]
   [dragonfly.player :refer [init-player init-controls update-player controls]]

   ["three/build/three.module.js" :as three]))

(set! *warn-on-infer* true)

(def ^js clock (three/Clock.))

(defn ^:dev/after-load animate []
  (def ^js delta (.getDelta clock))

  (.update controls delta)

  (js/requestAnimationFrame animate)

  (update-player)
  (update-world)

  (.render composer))

(defn init []
  (init-prelude)
  (init-camera)
  (init-render)
  (init-world)
  (init-player)
  (init-controls)
  (animate)

  (def handleResize (js/ResizeObserver.
                     (fn []
                       (set! (.-aspect camera) (/ (.-clientWidth  container)
                                                  (.-clientHeight container)))
                       (.updateProjectionMatrix camera)
                       (.setSize renderer
                                 (.-clientWidth  container)
                                 (.-clientHeight container)))))
  (.observe handleResize container))
