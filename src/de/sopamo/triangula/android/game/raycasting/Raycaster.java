package de.sopamo.triangula.android.game.raycasting;

import android.util.Log;
import de.sopamo.triangula.android.game.GameImpl;
import de.sopamo.triangula.android.geometry.GameShape;
import de.sopamo.triangula.android.particles.ParticleSpawner;
import org.jbox2d.collision.Segment;
import org.jbox2d.collision.SegmentCollide;
import org.jbox2d.common.RaycastResult;
import org.jbox2d.common.Vec2;
import org.jbox2d.common.XForm;

public class Raycaster {
    public static void cast() {
        GameImpl.getInstance().removeRays();
        /*Vec2 player = GameImpl.getMainPlayer().getBody().getWorldCenter();
        for(int i = 0;i<20;i++) {

            float rayX = 10 * (float)Math.cos(2*i*(Math.PI/20));
            float rayY = 10 * (float)Math.sin(2*i*(Math.PI/20));
            Segment segment = new Segment();
            segment.p1 = player;
            segment.p2 = new Vec2(player.x + rayX, rayY + player.y);
            RaycastResult finalResult = new RaycastResult();
            Float dist = null;
            for(GameShape gameShape: GameImpl.getInstance().getGsl()) {
                RaycastResult raycastResult = new RaycastResult();
                SegmentCollide res = gameShape.getShape().testSegment(gameShape.getShape().getBody().getXForm(),raycastResult,segment,0.01f);
                if(res == SegmentCollide.HIT_COLLIDE) {
                    if(dist == null || raycastResult.lambda < dist) {
                        dist = raycastResult.lambda;
                        finalResult.set(raycastResult);
                        Ray ray = new Ray(segment.p1, segment.p2);
                        GameImpl.getInstance().addRay(ray);
                    }
                }
            }
            if(dist != null) {
                Vec2 endPos = finalResult.normal.mul(finalResult.lambda);
                Ray ray2 = new Ray(player, endPos);
                GameImpl.getInstance().addRay(ray2);
            }
        }*/
    }
}
