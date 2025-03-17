(ns dragonfly.player
  (:require
   [dragonfly.prelude :refer [container scene]]
   [dragonfly.camera :refer [camera]]
   [dragonfly.render :refer [renderer]]
   [dragonfly.world :refer [world]]

   ["three/build/three.module.js" :as three]
   ["three/examples/jsm/controls/FlyControls.js" :refer (FlyControls)]
   ["cannon-es/dist/cannon-es.js" :as cannon]))

(defn init-player []
  (def ^js player-model (three/Mesh.
                         (three/CapsuleGeometry. 1 1 4 8)
                         (three/MeshBasicMaterial. (clj->js {:color 0xffffff}))))
  (def ^js player-hitbox (cannon/Box. (cannon/Vec3. 1 1 1)))
  (def ^js player-body (cannon/Body. (clj->js {:mass 1})))

  (.addShape player-body player-hitbox)
  (.addBody world player-body)
  (set! (.-y (.-position player-body)) 10)
  (.add scene player-model))

(defn init-controls []
  (def ^js controls (FlyControls. camera (.-domElement renderer)))

  (set! (.-movementSpeed controls) 100)
  (set! (.-domElement controls) (.-domElement renderer))
  (set! (.-rollSpeed controls) 1)
  (set! (.-autoForward controls) false)
  (set! (.-dragToLook controls) true))

(defn update-player []
  (.copy (.-position player-model) (.-position player-body))
  (.copy (.-quaternion player-model) (.-quaternion player-body)))
