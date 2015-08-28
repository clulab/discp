package edu.arizona.sista.discp

import org.springframework.core.io.Resource

/**
 * Service for accessing Document publications and their meta-information.
 *   Last modified: Initial creation.
 */
class DocumentService extends ServiceBase implements FilenameFilter {

  // injected resource pointing to input text files directory
  Resource textResource


  /** This class implements java.io.FilenameFilter. */
  boolean accept (java.io.File dir, java.lang.String filename) {
    return filename.endsWith('.txt')
  }


  /** Return list of Documents. */
  def getDocumentList () {
    log.debug("(DocumentService.getDocumentList):")
    def fileList = []
    if (textResource) {
      def textDir = textResource.getFile()
      if (textDir)
        fileList = (textDir.list(this) as List)
    }
    log.debug("(DocumentService.getDocumentList): => ${fileList}")
    return fileList
  }


  /** Return text of document with the given filename. */
  def getDocument (docName) {
    log.debug("(DocumentService.getDocument): docName=${docName}")

    Resource docResource = textResource.createRelative(docName)
    if (!docResource || !docResource.exists())
      return null

    def docFile = docResource.getFile()
    if (!docFile || !docFile.canRead())
      return null

    def text = docFile.getText()            // get file contents as text
    log.debug("(DocumentService.getDocument): => ${text}")
    return text
  }



  /** Return list of PubMed IDs. Not yet implemented. */
  def getPubMedList () {
    return []                               // not yet implemented
  }

  /** Return text of publication with given ID. */
  def getPublicationWithId (id) {
    return null                             // not yet implemented
  }

}
