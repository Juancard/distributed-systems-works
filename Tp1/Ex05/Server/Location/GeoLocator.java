package Tp1.Ex05.Server.Location;

import com.maxmind.geoip.LookupService;
import com.maxmind.geoip.regionName;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * User: juan
 * Date: 20/03/17
 * Time: 22:42
 */
public class GeoLocator {

    private static final String DATABASE_PATH = "/Tp1/Ex05/Server/Resources/GeoLiteCity.dat";
    private File database;

    public GeoLocator() throws IOException {
        this.database = loadDatabase();
    }

    private File loadDatabase() throws IOException {
        InputStream in = this.getClass().getResourceAsStream(DATABASE_PATH);
        try {
            File tempFile = File.createTempFile("geodatabase", ".tmp");
            tempFile.deleteOnExit();
            IOUtils.copy(in, new FileOutputStream(tempFile));
            return tempFile;
        } catch (IOException e) {
            throw new IOException("Error loading geo database: " + e.getMessage());
        }
    }

    public Location getLocation(String ipAddress) throws IOException{
        LookupService lookup = null;
        try {
            lookup = new LookupService(this.database, LookupService.GEOIP_MEMORY_CACHE);
        } catch (IOException e) {
            throw new IOException("No GeoLiteCity.dat found. Make sure it is located in: " + DATABASE_PATH);
        }
        com.maxmind.geoip.Location locationServices = lookup.getLocation(ipAddress);
        return createNewLocation(locationServices);
    }

    private static Location createNewLocation(com.maxmind.geoip.Location locationServices){
        Location location = new Location();

        location.setCountryCode(locationServices.countryCode);
        location.setCountryName(locationServices.countryName);
        location.setRegion(locationServices.region);
        location.setRegionName(regionName.regionNameByCode(
                locationServices.countryCode, locationServices.region));
        location.setCity(locationServices.city);
        location.setPostalCode(locationServices.postalCode);
        location.setLatitude(String.valueOf(locationServices.latitude));
        location.setLongitude(String.valueOf(locationServices.longitude));

        return location;
    }

}