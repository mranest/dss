/**
 * DSS - Digital Signature Services
 * Copyright (C) 2015 European Commission, provided under the CEF programme
 * 
 * This file is part of the "DSS - Digital Signature Services" project.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package eu.europa.esig.dss.validation;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import eu.europa.esig.dss.enumerations.DigestAlgorithm;
import eu.europa.esig.dss.enumerations.EncryptionAlgorithm;
import eu.europa.esig.dss.enumerations.MaskGenerationFunction;
import eu.europa.esig.dss.enumerations.SignatureAlgorithm;
import eu.europa.esig.dss.enumerations.SignatureForm;
import eu.europa.esig.dss.enumerations.SignatureLevel;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.x509.CertificateToken;
import eu.europa.esig.dss.spi.x509.SerialInfo;
import eu.europa.esig.dss.validation.scope.SignatureScope;
import eu.europa.esig.dss.validation.scope.SignatureScopeFinder;
import eu.europa.esig.dss.validation.timestamp.TimestampSource;
import eu.europa.esig.dss.validation.timestamp.TimestampToken;

/**
 * Provides an abstraction for an Advanced Electronic Signature. This ease the validation process. Every signature
 * format : XAdES, CAdES and PAdES are treated the same.
 */
public interface AdvancedSignature extends Serializable {

	/**
	 * This method returns the signature filename (useful for ASiC and multiple signature files)
	 * 
	 * @return the signature filename
	 */
	String getSignatureFilename();

	/**
	 * This method allows to set the signature filename (useful in case of ASiC)
	 */
	void setSignatureFilename(String signatureFilename);

	/**
	 * @return in the case of the detached signature this is the {@code List} of signed contents.
	 */
	List<DSSDocument> getDetachedContents();

	/**
	 * This method allows to set the signed contents in the case of the detached signature.
	 *
	 * @param detachedContents
	 *            {@code List} of {@code DSSDocument} representing the signed detached contents.
	 */
	void setDetachedContents(final List<DSSDocument> detachedContents);
	
	/**
	 * @return in case of ASiC signature returns a list of container documents
	 */
	List<DSSDocument> getContainerContents();
	
	/**
	 * This method allows to set the container contents in the case of ASiC signature.
	 *
	 * @param containerContents
	 *            {@code List} of {@code DSSDocument} representing the container contents.
	 */
	void setContainerContents(final List<DSSDocument> containerContents);

	/**
	 * This method allows to set the manifest files in the case of ASiC-E signature.
	 *
	 * @param manifestFiles
	 *            {@code List} of {@code ManifestFile}s
	 */
	void setManifestFiles(List<ManifestFile> manifestFiles);

	/**
	 * @return in case of ASiC-E signature returns a list of {@link DSSDocument}s contained in the related signature manifest
	 */
	List<DSSDocument> getManifestedDocuments();

	/**
	 * @return This method returns the provided signing certificate or {@code null}
	 */
	CertificateToken getProvidedSigningCertificateToken();

	/**
	 * This method allows to provide a signing certificate to be used in the validation process. It can happen in the
	 * case of a non-AdES signature without the signing certificate
	 * within the signature.
	 *
	 * @param certificateToken
	 *            {@code CertificateToken} representing the signing certificate token.
	 */
	void setProvidedSigningCertificateToken(final CertificateToken certificateToken);

	/**
	 * Specifies the format of the signature
	 */
	SignatureForm getSignatureForm();

	/**
	 * Retrieves the signature algorithm (or cipher) used for generating the signature.
	 *
	 * @return {@code SignatureAlgorithm}
	 */
	SignatureAlgorithm getSignatureAlgorithm();

	/**
	 * Retrieves the encryption algorithm used for generating the signature.
	 *
	 * @return {@code EncryptionAlgorithm}
	 */
	EncryptionAlgorithm getEncryptionAlgorithm();

	/**
	 * Retrieves the digest algorithm used for generating the signature.
	 *
	 * @return {@code DigestAlgorithm}
	 */
	DigestAlgorithm getDigestAlgorithm();

	/**
	 * Retrieves the mask generation function used for generating the signature.
	 *
	 * @return {@code MaskGenerationFunction}
	 */
	MaskGenerationFunction getMaskGenerationFunction();

	/**
	 * Returns the signing time included within the signature.
	 *
	 * @return {@code Date} representing the signing time or null
	 */
	Date getSigningTime();

	/**
	 * Gets a certificate source which contains ALL certificates embedded in the signature.
	 *
	 * @return
	 */
	SignatureCertificateSource getCertificateSource();

	/**
	 * Gets a ListCertificateSource representing a merged source from {@code signatureCertificateSource} and 
	 * all included to the signature timestamp objects
	 * 
	 * @return {@link ListCertificateSource}
	 */
	ListCertificateSource getCompleteCertificateSource();

	/**
	 * Gets a CRL source which contains ALL CRLs embedded in the signature.
	 *
	 * @return {@code SignatureCRLSource}
	 */
	SignatureCRLSource getCRLSource();

	/**
	 * Gets an OCSP source which contains ALL OCSP responses embedded in the signature.
	 *
	 * @return {@code SignatureOCSPSource}
	 */
	SignatureOCSPSource getOCSPSource();
	
	/**
	 * Gets a ListCRLSource representing a merged source from {@code signatureCRLSourse} and 
	 * all included to the signature timestamp objects
	 * 
	 * @return {@link ListCRLSource}
	 */
	ListCRLSource getCompleteCRLSource();
	
	/**
	 * Gets a ListOCSPSource representing a merged source from {@code signatureOCSPSourse} and 
	 * all included to the signature timestamp objects
	 * 
	 * @return {@link ListOCSPSource}
	 */
	ListOCSPSource getCompleteOCSPSource();
	
	/**
	 * Gets a Signature Timestamp source which contains ALL timestamps embedded in the signature.
	 *
	 * @return {@code SignatureTimestampSource}
	 */
	TimestampSource getTimestampSource();

	/**
	 * Gets an object containing the signing certificate or information indicating why it is impossible to extract it
	 * from the signature. If the signing certificate is identified then it is cached and the subsequent calls to this
	 * method will return this cached value. This method never returns null.
	 *
	 * @return
	 */
	CandidatesForSigningCertificate getCandidatesForSigningCertificate();

	/**
	 * This setter allows to indicate the master signature. It means that this is a countersignature.
	 *
	 * @param masterSignature
	 *            {@code AdvancedSignature}
	 */
	void setMasterSignature(final AdvancedSignature masterSignature);

	/**
	 * @return {@code AdvancedSignature}
	 */
	AdvancedSignature getMasterSignature();

	/**
	 * This method returns the signing certificate token or null if there is no valid signing certificate. Note that to
	 * determinate the signing certificate the signature must be
	 * validated: the method {@code checkSignatureIntegrity} must be called.
	 *
	 * @return
	 */
	CertificateToken getSigningCertificateToken();

	/**
	 * Verifies the signature integrity; checks if the signed content has not been tampered with. In the case of a
	 * non-AdES signature no including the signing certificate then the latter must be provided by calling
	 * {@code setProvidedSigningCertificateToken} In the case of a detached signature the signed content must be
	 * provided by calling {@code setProvidedSigningCertificateToken}
	 */
	void checkSignatureIntegrity();

	/**
	 * @return SignatureCryptographicVerification with all the information collected during the validation process.
	 */
	SignatureCryptographicVerification getSignatureCryptographicVerification();

	/**
	 * This method checks the protection of the certificates included within the signature (XAdES: KeyInfo) against the
	 * substitution attack.
	 */
	void checkSigningCertificate();

	/**
	 * Returns the Signature Policy OID from the signature.
	 *
	 * @return {@code SignaturePolicy}
	 */
	SignaturePolicy getPolicyId();

	/**
	 * Returns information about the place where the signature was generated
	 *
	 * @return {@code SignatureProductionPlace}
	 */
	SignatureProductionPlace getSignatureProductionPlace();

	/**
	 * This method obtains the information concerning commitment type indication linked to the signature
	 *
	 * @return a list of {@code CommitmentTypeIndication}s
	 */
	List<CommitmentTypeIndication> getCommitmentTypeIndications();

	/**
	 * Returns the value of the signed attribute content-type
	 *
	 * @return content type as {@code String}
	 */
	String getContentType();

	/**
	 * Returns the value of the signed attribute mime-type
	 *
	 * @return mime type as {@code String}
	 */
	String getMimeType();

	/**
	 * @return content identifier as {@code String}
	 */
	String getContentIdentifier();

	/**
	 * @return content hints as {@code String}
	 */
	String getContentHints();

	/**
	 * Returns the list of roles of the signer.
	 *
	 * @return list of the {@link SignerRole}s
	 */
	List<SignerRole> getSignerRoles();

	/**
	 * Returns the claimed roles of the signer.
	 *
	 * @return list of the {@link SignerRole}s
	 */
	List<SignerRole> getClaimedSignerRoles();

	/**
	 * Returns the certified roles of the signer.
	 *
	 * @return list of the {@link SignerRole}s
	 */
	List<SignerRole> getCertifiedSignerRoles();

	/**
	 * Get certificates embedded in the signature
	 *
	 * @return a list of certificate contained within the signature
	 */
	List<CertificateToken> getCertificates();

	/**
	 * Returns the content timestamps
	 *
	 * @return {@code List} of {@code TimestampToken}
	 */
	List<TimestampToken> getContentTimestamps();

	/**
	 * Returns the signature timestamps
	 *
	 * @return {@code List} of {@code TimestampToken}
	 */
	List<TimestampToken> getSignatureTimestamps();

	/**
	 * Returns the time-stamp which is placed on the digital signature (XAdES example: ds:SignatureValue element), the
	 * signature time-stamp(s) present in the AdES-T form, the certification path references and the revocation status
	 * references.
	 *
	 * @return {@code List} of {@code TimestampToken}
	 */
	List<TimestampToken> getTimestampsX1();

	/**
	 * Returns the time-stamp which is computed over the concatenation of CompleteCertificateRefs and
	 * CompleteRevocationRefs elements (XAdES example).
	 *
	 * @return {@code List} of {@code TimestampToken}
	 */
	List<TimestampToken> getTimestampsX2();

	/**
	 * Returns the archive Timestamps
	 *
	 * @return {@code List} of {@code TimestampToken}s
	 */
	List<TimestampToken> getArchiveTimestamps();
	
	/**
	 * Returns a list of timestamps defined with the 'DocTimeStamp' type
	 * NOTE: applicable only for PAdES
	 * @return {@code List} of {@code TimestampToken}s
	 */
	List<TimestampToken> getDocumentTimestamps();

	/**
	 * Returns a list of all timestamps found in the signature
	 * 
	 * @return {@code List} of {@code TimestampToken}s
	 */
	List<TimestampToken> getAllTimestamps();

	/**
	 * This method allows to add an external timestamp. The given timestamp must be processed before.
	 * 
	 * @param timestamp
	 *            the timestamp token
	 */
	void addExternalTimestamp(TimestampToken timestamp);

	/**
	 * Returns a list of counter signatures applied to this signature
	 *
	 * @return a {@code List} of {@code AdvancedSignatures} representing the counter signatures
	 */
	List<AdvancedSignature> getCounterSignatures();
	
	/**
	 * This method returns the {@link SignatureIdentifier}.
	 * 
	 * @return unique {@link SignatureIdentifier}
	 */
	SignatureIdentifier getDSSId();

	/**
	 * This method returns the DSS unique signature id. It allows to unambiguously identify each signature.
	 *
	 * @return The signature unique Id
	 */
	String getId();
	
	/**
	 * This method returns an identifier provided by the Driving Application (DA)
	 * Note: used only for XAdES
	 * 
	 * @return The signature identifier
	 */
	String getDAIdentifier();

	/**
	 * @param signatureLevel
	 *            {@code SignatureLevel} to be checked
	 * @return true if the signature contains the data needed for this {@code SignatureLevel}. Doesn't mean any validity
	 *         of the data found.
	 */
	boolean isDataForSignatureLevelPresent(final SignatureLevel signatureLevel);

	SignatureLevel getDataFoundUpToLevel();

	/**
	 * @return the list of signature levels for this type of signature, in the simple to complete order. Example:
	 *         B,T,LT,LTA
	 */
	SignatureLevel[] getSignatureLevels();
	
	/**
	 * Checks if all certificate chains present in the signature are self-signed
	 * @return TRUE if all certificates are self-signed, false otherwise
	 */
	boolean areAllSelfSignedCertificates();

	void prepareTimestamps(ValidationContext validationContext);

	/**
	 * This method allows the structure validation of the signature.
	 */
	void validateStructure();

	String getStructureValidationResult();

	void checkSignaturePolicy(SignaturePolicyProvider signaturePolicyDetector);

	void findSignatureScope(SignatureScopeFinder signatureScopeFinder);

	List<SignatureScope> getSignatureScopes();
	
	/**
	 * Returns true if the validation of the signature has been performed only on Signer's Document Representation (SDR).
	 * (An SDR typically is built on a cryptographic hash of the Signer's Document)
	 * @return true of it is DocHashOnly validation, false otherwise
	 */
	boolean isDocHashOnlyValidation();
	
	/**
	 * Returns true if the validation of the signature has been performed only on Data To Be Signed Representation (DTBSR).
	 * 
	 * EN 319 102-1 v1.1.1 (4.2.8 Data to be signed representation (DTBSR)):
	 * The DTBS preparation component shall take the DTBSF and hash it according to the hash algorithm specified in the
	 * cryptographic suite. The result of this process is the DTBSR, which is then used to create the signature. 
	 * NOTE: In order for the produced hash to be representative of the DTBSF, the hashing function has the property 
	 * that it is computationally infeasible to find collisions for the expected signature lifetime. Should the hash
	 * function become weak in the future, additional security measures, such as applying time-stamp tokens,
	 * can be taken. 
	 * @return true of it is HashOnly validation, false otherwise
	 */
	boolean isHashOnlyValidation();
	
	/**
	 * Returns the digital signature value
	 * @return digital signature value byte array
	 */
	byte[] getSignatureValue();

	/**
	 * Returns individual validation foreach reference (XAdES) or for the
	 * message-imprint (CAdES)
	 * 
	 * @return a list with one or more {@code ReferenceValidation}
	 */
	List<ReferenceValidation> getReferenceValidations();
	
	/**
	 * Returns a signature reference element as defined in TS 119 442 - V1.1.1 - 
	 * Electronic Signatures and Infrastructures (ESI), ch. 5.1.4.2.1.3 XML component
	 * 
	 * @param digestAlgorithm {@link DigestAlgorithm} to use
	 * @return {@link SignatureDigestReference}
	 */
	SignatureDigestReference getSignatureDigestReference(DigestAlgorithm digestAlgorithm);

	// ------------------------ CAdES Specifics for TS 119 102-2

	/**
	 * Returns a digest value incorporated in an attribute "message-digest" in CMS Signed Data
	 * 
	 * @return a byte array representing a signed content digest value
	 */
	byte[] getMessageDigestValue();
	
	/**
	 * Returns a list of SerialInfos extracted from a SignerInformationStore of CMS Signed Data
	 * 
	 * @return a list of {@link SerialInfo}s
	 */
	List<SerialInfo> getSignerInformationStoreInfos();

	// ------------------------ PDF Specifics for TS 119 102-2
	
	/**
	 * Retrieves a PdfRevision (PAdES) related to the current signature
	 * 
	 * @return {@link PdfRevision}
	 */
	PdfRevision getPdfRevision();

}
