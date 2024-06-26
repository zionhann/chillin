package com.chillin.epson

import com.chillin.adobe.AdobeService
import com.chillin.drawing.DrawingService
import com.chillin.s3.S3Service
import com.chillin.type.DrawingType
import com.chillin.type.MediaSubtype
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.util.*

@RestController
class EpsonConnectController(
    private val drawingService: DrawingService,
    private val s3Service: S3Service,
    private val adobeService: AdobeService
) {
    private val logger = LoggerFactory.getLogger(EpsonConnectController::class.java)

    @PostMapping("/scan")
    fun receiveScanData(@RequestParam files: Map<String, MultipartFile>) {
        logger.info("Received {} files={}", files.values.size, files.values.map(MultipartFile::getOriginalFilename))

        files.values.forEach { file ->
            val mediaSubtype = MediaSubtype.parse(file.contentType)
            val pathname = "scanned/${UUID.randomUUID()}.${mediaSubtype.value}"

            val downloadUrl = s3Service.uploadImage(pathname, file)
            val uploadUrl = s3Service.getImageUrlForPOST(pathname)

            adobeService.cutout(downloadUrl, uploadUrl)
            drawingService.save(pathname, DrawingType.SCANNED)
        }
    }
}