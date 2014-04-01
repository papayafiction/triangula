/* 
 * File:   JNIDebugDraw.h
 * Author: stevijo
 *
 * Created on 31. März 2014, 01:22
 */

#ifndef JNIDEBUGDRAW_H
#define	JNIDEBUGDRAW_H

class JNIDebugDraw : public b2Draw {
public:
    JNIDebugDraw();
    JNIDebugDraw(const JNIDebugDraw& orig);
    virtual ~JNIDebugDraw();
    void DrawPolygon(const b2Vec2* vertices, int32 vertexCount, const b2Color& color);
    void DrawSolidPolygon(const b2Vec2* vertices, int32 vertexCount, const b2Color& color);
    void DrawCircle(const b2Vec2& center, float32 radius, const b2Color& color);
    void DrawSolidCircle(const b2Vec2& center, float32 radius, const b2Vec2& axis, const b2Color& color);
    void DrawSegment(const b2Vec2& p1, const b2Vec2& p2, const b2Color& color);
    void DrawTransform(const b2Transform& xf);
private:

};

#endif	/* JNIDEBUGDRAW_H */

