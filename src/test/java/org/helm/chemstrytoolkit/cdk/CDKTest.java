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
package org.helm.chemstrytoolkit.cdk;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.helm.chemtoolkit.AbstractChemistryManipulator.StType;
import org.helm.chemtoolkit.AbstractMolecule;
import org.helm.chemtoolkit.Attachment;
import org.helm.chemtoolkit.AttachmentList;
import org.helm.chemtoolkit.CTKException;
import org.helm.chemtoolkit.CTKSmilesException;
import org.helm.chemtoolkit.ManipulatorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

/**
 * @author chistyakov
 *
 */
public class CDKTest extends TestBase {

  private static final Logger LOG = LoggerFactory.getLogger(CDKTest.class);

  private final String ClassName = "org.helm.chemtoolkit.cdk.CDKManipulator";

  @BeforeSuite(groups = {"CDKTest"})
  public void initialize() throws CTKException, IOException {

    try {
      LOG.debug("initialize");
      manipulator = ManipulatorFactory.buildManipulator(ClassName);
    } catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException
        | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
      throw new CTKException("unable to invoke a manipulator");
    }

    if (!Files.exists(Paths.get("test-output"))) {
      Files.createDirectories(Paths.get("test-output"));
    }
  }

  @Override
  @Test(groups = {"CDKTest"})
  public void validateSMILESTest() throws CTKException {
    super.validateSMILESTest();
  }

  @Override
  @Test(groups = {"CDKTest"})
  public void getMoleculeInfoTest() throws CTKException, IOException {
    super.getMoleculeInfoTest();
    Assert.assertEquals(testResult, "C21H28N6O4S");

  }

  @Override
  @Test(groups = {"CDKTest"})
  public void convertSMILES2MolFile() throws CTKException, Exception {
    super.convertSMILES2MolFile();
    LOG.debug(testResult);

  }

  @Override
  @Test(groups = {"CDKTest"})
  public void convertMolFile2SMILES() throws CTKException, IOException {
    super.convertMolFile2SMILES();
    LOG.debug("canonical=" + testResult);
    Assert.assertEquals(testResult, manipulator.canonicalize("C([C@@H]1[C@H]([C@H]([C@]([H])(N2C=NC3=C2N=CN=C3N)O1)O)O*)O*"));

  }

  @Override
  @Test(groups = {"CDKTest"})
  public void canonicalizeTest() throws CTKSmilesException, CTKException {
    super.canonicalizeTest();
    Assert.assertEquals(testResult, manipulator.canonicalize("O=C1NC(=NC=2C(=NN(C12)C)CC)C=3C=C(C=CC3OCC)S(=O)(=O)N4CCN(C)CC4"));

  }

  @Override
  @Test(groups = {"CDKTest"})
  public void renderMolTest() throws CTKException, IOException {
    super.renderMolTest();
  }

  @Override
  @Test(groups = {"CDKTest"})
  public void renderSequenceTest() throws NumberFormatException, CTKException, IOException {
    super.renderSequenceTest();
  }

  @Override
  @Test(groups = {"CDKTest"})
  public void createGroupMolFile() throws CTKException, IOException {
    super.createGroupMolFile();
    LOG.debug(testResult);
  }

  @Override
  @Test(groups = {"CDKTest"})
  public void adeninRiboseMerge() throws IOException, CTKException {
    super.adeninRiboseMerge();
    LOG.debug(testResult);
  }

  @Override
  @Test(groups = {"CDKTest"})
  public void mergeTest() throws ClassNotFoundException, NoSuchMethodException, SecurityException,
      InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException,
      CTKException {
    super.mergeTest();
    LOG.debug(testResult);

  }

  @Override
  @Test(groups = {"CDKTest"})
  public void getRibose() throws IOException, CTKException {
    super.getRibose();
    Assert.assertEquals(testResult, "O[C@H]1[C@@H](O[C@H](CO[H])[C@H]1O[H])O");
  }

  @Override
  @Test(groups = {"CDKTest"})
  public void aaMerge() throws IOException, CTKException {
    super.aaMerge();
  }

  @Override
  @Test(groups = {"CDKTest"})
  public void mergePhosphatRiboseTest() throws ClassNotFoundException, NoSuchMethodException, SecurityException,
      InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException,
      CTKException {
    super.mergePhosphatRiboseTest();
    LOG.debug(testResult);
  }

  @Override
  @Test(groups = {"CDKTest"})
  public void mergeSelfCycle() throws CTKException, IOException {
    super.mergeSelfCycle();
    LOG.debug(testResult);

  }

  @Override
  @Test(groups = {"CDKTest"}, expectedExceptions = CTKException.class)
  public void merge2Ribose() throws IOException, CTKException {
    String ribose = "O[C@H]1[C@H]([*])O[C@H](CO[*])[C@H]1O[*] |$;;;_R3;;;;;_R1;;;_R2$|";
	  
    ribose = manipulator.convertExtendedSmiles(ribose);
    String riboseR1 = "[*][H] |$_R1;$|";
    String riboseR2 = "[*][H] |$_R2;$|";
    String riboseR3 = "O[*] |$;_R3$|";
    
    riboseR1 = manipulator.convertExtendedSmiles(riboseR1);
    riboseR2 = manipulator.convertExtendedSmiles(riboseR2);
    riboseR3 = manipulator.convertExtendedSmiles(riboseR3);

    AttachmentList groupsRibose = new AttachmentList();

    groupsRibose.add(new Attachment("R1-H", "R1", "H", riboseR1));
    groupsRibose.add(new Attachment("R2-H", "R2", "H", riboseR2));
    groupsRibose.add(new Attachment("R3-OH", "R3", "OH", riboseR3));

    AbstractMolecule ribose1 = manipulator.getMolecule(ribose, groupsRibose);

    AbstractMolecule ribose2 = manipulator.getMolecule(ribose, groupsRibose.cloneList());

    @SuppressWarnings("unused")
    AbstractMolecule molecule =
        manipulator.merge(ribose1, ribose1.getRGroupAtom(3, true), ribose2, ribose2.getRGroupAtom(3, true));
    System.out.println(manipulator.convertMolecule(molecule, StType.SMILES));

  }

  @Test(groups = {"CDKTest"})
  public void getMolecule() throws IOException, CTKException {
    String ribose = "O[C@H]1[C@H]([*])O[C@H](CO[*])[C@H]1O[*]";
    // String ribose = "O[C@H]1[C@H](O[C@H](CO[H])[C@H]1O[H])O";
    AbstractMolecule molecule = manipulator.getMolecule(ribose, null);
    String res = manipulator.convertMolecule(molecule, StType.SMILES);
    LOG.debug(res);

  }
  
  

  
  
  

}
