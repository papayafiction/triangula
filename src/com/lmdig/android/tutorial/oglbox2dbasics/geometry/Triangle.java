/*
 *
 * (c)2010 Lein-Mathisen Digital
 * http://lmdig.com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 2 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.  
 *
 */



package com.lmdig.android.tutorial.oglbox2dbasics.geometry;

public class Triangle {

    private float size;

	public Triangle() {

	}
	public Triangle(float size) {
        this.size = size;
		onUpdateSize();
	}

    public float getSize() {
        return size;
    }

    public void setSize(float size) {
        this.size = size;
    }

    protected void onUpdateSize() {
	}
}
