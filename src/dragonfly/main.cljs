(ns dragonfly.main
  (:require
   ["three/build/three.module.js" :as three]
   ["cannon-es/dist/cannon-es.js" :as cannon]))

(set! *warn-on-infer* true)

(def ^js container (.getElementById js/document "dragonfly"))

;; three

(def ^js camera (three/PerspectiveCamera. 75
                                          (/ (.-clientWidth  container)
                                             (.-clientHeight container))
                                          1
                                          100))
(set! (.. camera -position -z) 5)

(def ^js scene (three/Scene.))

(def ^js renderer (three/WebGLRenderer.))
(.setSize renderer
          (.-clientWidth  container)
          (.-clientHeight container))
(.appendChild container (.-domElement renderer))

(def ^js geometry (three/BoxGeometry. 2 2 2))

(def ^js material (three/MeshBasicMaterial. (clj->js {:color 0x00ff00})))

(def ^js cube (three/Mesh. geometry material))
(.add scene cube)

;; cannon

(def ^js world (cannon/World.))

(def ^js box (cannon/Box. (cannon/Vec3. 1 1 1)))

(def ^js body (cannon/Body. (clj->js {:mass 1})))

(.addShape body box)
(.set (.-angularVelocity body) 0 10 0)
(set! (.-angularDamping body) 0.5)
(.addBody world body)

(def handleResize (js/ResizeObserver.
                   (fn []
                     (set! (.-aspect camera) (/ (.-clientWidth  container)
                                                (.-clientHeight container)))
                     (.updateProjectionMatrix camera)
                     (.setSize renderer
                               (.-clientWidth  container)
                               (.-clientHeight container)))))
(.observe handleResize container)

(defn animate []
  (js/requestAnimationFrame animate)

  (.fixedStep world)
  (.copy (.-position cube) (.-position body))
  (.copy (.-quaternion cube) (.-quaternion body))
  (.render renderer scene camera))
