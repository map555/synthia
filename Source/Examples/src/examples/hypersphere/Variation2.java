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

import ca.uqac.lif.synthia.random.GaussianFloat;
import ca.uqac.lif.synthia.util.Constant;
import ca.uqac.lif.synthia.vector.HyperspherePicker;

/**
 * Generates two-dimensional points lying at right angles along a randomly
 * selected circle. The points are generated by composing pickers as in the
 * following diagram:
 * <p>
 * <img src="{@docRoot}/doc-files/hypersphere/Variation2.png" alt="Diagram"> 
 * <p>
 * This variation shows the use of the {@link Freeze} picker: a random float
 * value between 1 and 3 is picked for the radius, and this radius will then be
 * used for all points produced by the {@link HyperspherePicker}. The angle is
 * randomly selected among four values lying at intervals of &pi;/2. As a
 * result, different seeds will produce sets of points lying at a different
 * distance from the origin, but the same distance for each picker instance.
 * 
 * @ingroup Examples
 */
public class Variation2 
{

	@SuppressWarnings("unchecked")
	public static void main(String[] args)
	{
		GaussianFloat radius = new GaussianFloat();
		Constant<Float> angle = new Constant<Float>((float) (3 * Math.PI / 4));
		HyperspherePicker hp = new HyperspherePicker(radius, angle);
		for (int i = 0; i < 100; i++)
		{
			System.out.println(Variations.printPoint(hp.pick()));
		}
	}

}
