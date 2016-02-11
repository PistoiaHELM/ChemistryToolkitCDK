/*******************************************************************************
 * Copyright C 2015, The Pistoia Alliance
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the
 * Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 ******************************************************************************/
package org.helm.chemtoolkit.cdk;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.helm.chemtoolkit.AbstractMolecule;
import org.helm.chemtoolkit.AttachmentList;
import org.helm.chemtoolkit.CTKException;
import org.helm.chemtoolkit.IAtomBase;
import org.helm.chemtoolkit.IBondBase;
import org.helm.chemtoolkit.IChemObjectBase;
import org.openscience.cdk.aromaticity.Kekulization;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.IPseudoAtom;
import org.openscience.cdk.interfaces.IStereoElement;
import org.openscience.cdk.layout.StructureDiagramGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author chistyakov
 *
 */
public class CDKMolecule extends AbstractMolecule {
  private static final Logger LOG = LoggerFactory.getLogger(CDKMolecule.class);

  private IAtomContainer molecule;

  public CDKMolecule(IAtomContainer molecule) {
    this(molecule, new AttachmentList());

  }

  public CDKMolecule(IAtomContainer molecule, AttachmentList attachments) {
    this.molecule = molecule;
    if (attachments != null)
      setAttachments(attachments);
    else
      this.attachments = new AttachmentList();
    atoms = new ArrayList<>();
    for (IAtom atom : molecule.atoms()) {
      int rGroupId = 0;
      if (atom instanceof IPseudoAtom) {
        atom.setSymbol("R");
        rGroupId = AbstractMolecule.getIdFromLabel(((IPseudoAtom) atom).getLabel());
      }
      List<IBond> bonds = molecule.getConnectedBondsList(atom);
      atoms.add(new CDKAtom(atom, rGroupId, bonds));
    }

  }

  /**
   * 
   * {@inheritDoc}
   */
  @Override
  public Map<String, IAtomBase> getRgroups() throws CTKException {
    return super.getRgroups();
  }

  /**
   * 
   * {@inheritDoc}
   */

  @Override
  public void dearomatize() throws CTKException {

    try {
      Kekulization.kekulize(molecule);
    } catch (CDKException e) {
      throw new CTKException(e.getMessage(), e);
    }

  }

  /**
   * 
   * {@inheritDoc}
   */
  @Override
  public void removeINode(IAtomBase node) throws CTKException {
    if (node instanceof CDKAtom) {
      if (atoms.contains(node)) {
        molecule.removeAtomAndConnectedElectronContainers(((CDKAtom) node).atom);

        for (int i = 0; i < atoms.size(); i++) {
          if (((CDKAtom) atoms.get(i)).compare(node)) {
            atoms.remove(i);
            break;
          }
        }
      } else
        throw new CTKException("the atom not found in the molecule");
    } else
      throw new CTKException("invalid atom");

  }

  /**
   * 
   * {@inheritDoc}
   */
  @Override
  public void addIBase(IChemObjectBase object) {
    if (object instanceof CDKMolecule) {
      CDKMolecule container = (CDKMolecule) object;
      molecule.add(container.getMolecule());
      atoms.addAll(container.getIAtomArray());
    } else if (object instanceof CDKAtom) {
      CDKAtom atom = (CDKAtom) object;
      molecule.addAtom(atom.getMolAtom());
      atoms.add(atom);
    } else if (object instanceof CDKBond) {
      molecule.addBond(((CDKBond) object).bond);
    } else if (object instanceof CDKStereoElement) {
      molecule.addStereoElement(((CDKStereoElement) object).getStereoElement());
      molecule.addBond(((CDKStereoElement) object).getBond());
    }

  }

  /**
   * 
   * {@inheritDoc}
   */
  @Override
  public List<IBondBase> getIBondArray() {
    List<IBondBase> array = new ArrayList<>();

    for (IBond item : molecule.bonds()) {
      array.add(new CDKBond(item));
    }
    return array;
  }

  /**
   * 
   * {@inheritDoc}
   */
  @Override
  public AbstractMolecule cloneMolecule() throws CTKException {
    CDKMolecule cloned = null;
    try {
      cloned = new CDKMolecule(molecule.clone(), attachments.cloneList());
    } catch (CloneNotSupportedException e) {
      throw new CTKException(e.getMessage(), e);
    }
    return cloned;
  }

  /**
   * {@inheritDoc}
   * 
   * @throws CTKException
   */
  @Override
  public void generateCoordinates(int dem) throws CTKException {

    StructureDiagramGenerator sdg = new StructureDiagramGenerator();
    sdg.setMolecule(molecule);
    try {
      sdg.generateCoordinates();
    } catch (CDKException e) {
      throw new CTKException(e.getMessage(), e);
    }
    molecule = sdg.getMolecule();

  }

  /**
   * @return
   */
  @Override
  public IAtomContainer getMolecule() {
    return molecule;
  }

  /**
   * {@inheritDoc}
   * 
   * @throws CTKException
   */
  @Override
  public void changeAtomLabel(int index, int toIndex) throws CTKException {
    for (IAtomBase atom : getIAtomArray()) {
      if (atom.getFlag() != Flag.PROCESSED && atom.getMolAtom() instanceof IPseudoAtom) {
        int currIndex = AbstractMolecule.getIdFromLabel(((IPseudoAtom) atom.getMolAtom()).getLabel());
        if (currIndex == index)
          atom.setRgroup(toIndex);
      }
    }

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isSingleStereo(IAtomBase atom) throws CTKException {
    if (atom instanceof CDKAtom) {
      if (atom.getIBondCount() != 1) {
        throw new CTKException("RGroup is allowed to have single connection to other atom");
      }
      IAtom rAtom = (IAtom) atom.getMolAtom();
      for (IStereoElement element : molecule.stereoElements()) {
        if (element.contains(rAtom))
          return true;
      }
      return false;
    } else
      throw new CTKException("invalid atom");
  }



  /**
   * {@inheritDoc}
   */
  @Override
  public void removeIBase(IChemObjectBase object) {
    // TODO Auto-generated method stub

  }

}
