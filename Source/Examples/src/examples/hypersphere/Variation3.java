/*
    Synthia, a data structure generator
    Copyright (C) 2019-2021 Laboratoire d'informatique formelle
    Université du Québec à Chicoutimi, Canada

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published
    by the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package examples.hypersphere;

import ca.uqac.lif.synthia.random.AffineTransform.AffineTransformFloat;
import ca.uqac.lif.synthia.random.RandomFloat;
import ca.uqac.lif.synthia.util.Choice;
import ca.uqac.lif.synthia.util.Freeze;
import ca.uqac.lif.synthia.vector.HyperspherePicker;

/**
 * Generates two-dimensional points producing a spiral pattern.
 * The points are generated by composing pickers as in the following diagram:
 * <p>
 * <img src="{@docRoot}/doc-files/hypersphere/Variation3.png" alt="Diagram"> 
 * <p>
 * This variation shows the use of the {@link Tick} picker. The radius of the
 * first point is either 0 or 1, and each successive radius increments by a
 * fixed amount of &pi;/6. On its side, the starting angle is randomly selected
 * between four values, and each successive angle is incremented by a fixed
 * amount of &pi;/6. This results in a spiral pattern with slight variations.
 * 
 * @ingroup Examples
 */
public class Variation3 
{

	@SuppressWarnings("unchecked")
	public static void main(String[] args)
	{
		RandomFloat r_float1 = new RandomFloat().setSeed(42);
		RandomFloat r_float2 = new RandomFloat().setSeed(40);
		AffineTransformFloat af = new AffineTransformFloat(r_float1, 2, 1);
		Freeze<Float> radius = new Freeze<Float>(af);
		Choice<Double> angle = new Choice<Double>(r_float2)
				.add(0d, 0.25).add(Math.PI / 2, 0.25).add(Math.PI, 0.25).add(3 * Math.PI / 2, 0.25);
		HyperspherePicker hp = new HyperspherePicker(radius, angle);
		for (int i = 0; i < 100; i++)
		{
			System.out.println(Variations.printPoint(hp.pick()));
		}
	}

}
