/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc;

import java.io.FileInputStream;
import java.util.Properties;
import eazycommon.util.Util;

/**
 *
 * @author RuAc0n
 */
public class Config {

    public static int ServerPort = 9119;

//    public static String NotificationServerIP = "192.168.6.226";
    public static String NotificationServerIP = "localhost";
    public static int NotificationPort = 3221;

//    public static String UMSServerIP = "192.168.6.226";
    public static String UMSServerIP = "localhost";
    public static int UMSPort = 8090;

    public static String MeetPeopleServerIP = "localhost";
    public static int MeetPeopleServerPort = 2612;

    public static String BuzzServerIP = "localhost";
    public static int BuzzServerPort = 9115;

    public static String ChatServerIP = "localhost";
    public static int ChatServerPort = 9116;

    public static String BackEndServerIp = "localhost";
    public static int BackEndServerPort = 9120;

    public static String StaticFileServerIp = "localhost";
    public static int StaticFileServerPort = 9117;

//    public static String DB_SERVER = "192.168.6.222";
    public static String DB_SERVER = "localhost";
    public static int DB_PORT = 27017;

    public static int GabageCollector_ThreadNumber = 1;
    public static int SessionTimeOut = 180; //minutes
    public static int WebSessionTimeOut = 7; //day

    public static String REDIRECT_IOS_APP = "Meets://";
    public static String REDIRECT_IOS_APP_ENTERPRISE = "Meets://";
    public static String REDIRECT_ANDROID_STORE = "https://play.google.com/store/apps/details?id=app.net.fit";
    public static String REDIRECT_ANDROID_APP = "meets://com.apps.meets";
    public static String REDIRECT_IOS_7_STORE = "http://160.13.90.96/meets/appstore.html";

    public static String SIP_SERVER_IP = "202.32.203.169";

//    public static String ADMINISTRATOR_ACCOUNT = "administrator@gmail.com";
    public static String MOCOM_IP = "192.168.6.226";
//  public static String ANDROID_PURCHASES_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAtiTxJHJfVA/saAARiWxYtyMDkaWS/WlX3DbWc42/IqidKxm24PcTAVic2mZ65rmA4o0EiqBfLEe7vwLgiHE698INpf42VH1+O6sTl/OaQyygR/LZoNy04N4wxbaKGfdjsrrXhyySpCLNdc8EsiXUhrQ3pdIwmDVuKblZ8gouKbqdhIBwCH85vNFqNmlh9lOZFs8iryGSAbMveR89DM1w1hn1Iq0Zy2RFTSnOE7mMv0LhcZ556a66wPtasLpIq69SKK/5z87PoHnsHUUjq83IwKRZf7/MZEKM7wntblZRE2U2kyzwl4v3qm9NO01ZcJlgXPBKIrbzycVm/bMJv4zWWQIDAQAB";
    //public static String ANDROID_PURCHASES_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAyGqCuEN/b4CW8ZsDxo4aqFWWgqE52kCl6LCz+ea4qCYLuygTl3Oe7RBlG+xQ//MmoJNywkLmYDTXjJTo1WWuS1UQhpTawpWDEwMsr74pmHhxbEvuU8ytcq4CXoBmN43hwpoVI9kCrWMX7fZHo3w4z0aDz+g/F8FGm+SnLXfgpC0JR6AZpKf9/vn4Za/C6RF1f0baveNkZS+Y70VyEaWPpsYAsuYwRZAA2RlKaJkOS4DcusejLpw0GZciuoidub9zBi9ZG6el0NCxINFKz7psulaccvMU+ygypBY5gGNcw9O691Sjw35a9riX05ufX3XgsFHYpYZOmM2Sr332yHnX3wIDAQAB";
    public static String ANDROID_PURCHASES_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAzDx0bOCEImfpGAEem47rRWwja1wE04pBVdVwYPorWnL1BBwtdz3p0c6tFXEatrUR3qL8gAhfnVEUJfL68BiR+8KfA0gZK8xVpdO4DZKCT4wQhxo9obHzZzOHNL0beiLKr9gF/d9jLY7oFp8/CDq4Xj+0C5z1fewha1oyZHf0OmHojZktBEcGw7kgY0SGoqx5dIoGbBNQi5Vx06WFcGQC5+BsPH9dRl45U+cXfBp8k+ttfmeqjWUad6TorggisKbWrDcBUbzZ2czzXYqp1RL6gwwpaJdGtAwAWrXPx9mrn/mnkGBZc2JDadzj1xDIrAhZxCEk3TRWbtNEiYxSHyZcKwIDAQAB";
    public static String AMAZONE_PURCHASES_KEY = "2:0fNyCMW3KjXH5XX2sl_CNRm4MNXOYn1cGn2Z7ReoYMddMU8MrxkX0CsB1pBaXa5v:TIAjDiKwZBUKODpgmqWgaw==";

    public static String PUBLIC_KEY1 = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAzDx0bOCEImfpGAEem47rRWwja1wE04pBVdVwYPorWnL1BBwtdz3p0c6tFXEatrUR3qL8gAhfnVEUJfL68BiR+8KfA0gZK8xVpdO4DZKCT4wQhxo9obHzZzOHNL0beiLKr9gF/d9jLY7oFp8/CDq4Xj+0C5z1fewha1oyZHf0OmHojZktBEcGw7kgY0SGoqx5dIoGbBNQi5Vx06WFcGQC5+BsPH9dRl45U+cXfBp8k+ttfmeqjWUad6TorggisKbWrDcBUbzZ2czzXYqp1RL6gwwpaJdGtAwAWrXPx9mrn/mnkGBZc2JDadzj1xDIrAhZxCEk3TRWbtNEiYxSHyZcKwIDAQAB";
    public static String PUBLIC_KEY2 = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAmsY8v4fHNpbDi0e/V58NAeuv0p7ooTnh7N2+6do+M45Jejuqk0JXjPzorFSZrm9vQT2SWJf/Pu+kQ4ybHCPZ0SHYREom75NvEeRwY168LNWFZgJ+vXwOG3tsW5GDI6CmFkhiieCDGTCbni/KbuRcfluzFSoFWMDX3st8ZGxkwQlicDoK0AfRZF2wiMSdprccXCJmFfvEbl19Fu0+20xvK6ETnK/b/d5uIP/Qo6uWUiKM+9/Jkh1X05VCeom8UN2KkPQpaZOUg1WpFL8iTinlwtBR2H1WdNlNL+/KAYXSfn6B8EcFiHOxU1N4sTzBK+nXNYSSE80QC1lxo0EmOd3wAwIDAQAB";
//    public static String PUBLIC_KEY3 = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAzDx0bOCEImfpGAEem47rRWwja1wE04pBVdVwYPorWnL1BBwtdz3p0c6tFXEatrUR3qL8gAhfnVEUJfL68BiR+8KfA0gZK8xVpdO4DZKCT4wQhxo9obHzZzOHNL0beiLKr9gF/d9jLY7oFp8/CDq4Xj+0C5z1fewha1oyZHf0OmHojZktBEcGw7kgY0SGoqx5dIoGbBNQi5Vx06WFcGQC5+BsPH9dRl45U+cXfBp8k+ttfmeqjWUad6TorggisKbWrDcBUbzZ2czzXYqp1RL6gwwpaJdGtAwAWrXPx9mrn/mnkGBZc2JDadzj1xDIrAhZxCEk3TRWbtNEiYxSHyZcKwIDAQAB";
    public static String PUBLIC_KEY3 = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAryfvDEqI55iTUQLJdG6rDmMD8g7wrTpHOaSuuZjtGrWIlOJR7sVw8MQQfsPj5uiqSnC4Y8DDMLBNXyDgkl8x5URmj+sxYnrHWSCLOlTZX00O2g+n06x/FRLEr9QJXXoO/G8tVnxSSfYf/wImxkNUgCDN6zgdfxztbh+yQIdxqLPHs0o9kpbD99eBa8jU9MIqZSPjmbPNCgkIlbwbIc9v7iMaKu22ECxHTMO5OrS7ETAk6Lz3F0fg3z8mbVOVQT34j2M8miaOVPrj2tZAnVI7AV98Osu9yVyeHmj57x2zDUsVhmDP6joaVqxd4D+NpdUndvXqqxOSH66TKXNX09XKawIDAQAB";
    public static String PUBLIC_KEY4 = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAzDx0bOCEImfpGAEem47rRWwja1wE04pBVdVwYPorWnL1BBwtdz3p0c6tFXEatrUR3qL8gAhfnVEUJfL68BiR+8KfA0gZK8xVpdO4DZKCT4wQhxo9obHzZzOHNL0beiLKr9gF/d9jLY7oFp8/CDq4Xj+0C5z1fewha1oyZHf0OmHojZktBEcGw7kgY0SGoqx5dIoGbBNQi5Vx06WFcGQC5+BsPH9dRl45U+cXfBp8k+ttfmeqjWUad6TorggisKbWrDcBUbzZ2czzXYqp1RL6gwwpaJdGtAwAWrXPx9mrn/mnkGBZc2JDadzj1xDIrAhZxCEk3TRWbtNEiYxSHyZcKwIDAQAB";
    public static String PUBLIC_KEY5 = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAzDx0bOCEImfpGAEem47rRWwja1wE04pBVdVwYPorWnL1BBwtdz3p0c6tFXEatrUR3qL8gAhfnVEUJfL68BiR+8KfA0gZK8xVpdO4DZKCT4wQhxo9obHzZzOHNL0beiLKr9gF/d9jLY7oFp8/CDq4Xj+0C5z1fewha1oyZHf0OmHojZktBEcGw7kgY0SGoqx5dIoGbBNQi5Vx06WFcGQC5+BsPH9dRl45U+cXfBp8k+ttfmeqjWUad6TorggisKbWrDcBUbzZ2czzXYqp1RL6gwwpaJdGtAwAWrXPx9mrn/mnkGBZc2JDadzj1xDIrAhZxCEk3TRWbtNEiYxSHyZcKwIDAQAB";
    public static String PUBLIC_KEY6 = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAzDx0bOCEImfpGAEem47rRWwja1wE04pBVdVwYPorWnL1BBwtdz3p0c6tFXEatrUR3qL8gAhfnVEUJfL68BiR+8KfA0gZK8xVpdO4DZKCT4wQhxo9obHzZzOHNL0beiLKr9gF/d9jLY7oFp8/CDq4Xj+0C5z1fewha1oyZHf0OmHojZktBEcGw7kgY0SGoqx5dIoGbBNQi5Vx06WFcGQC5+BsPH9dRl45U+cXfBp8k+ttfmeqjWUad6TorggisKbWrDcBUbzZ2czzXYqp1RL6gwwpaJdGtAwAWrXPx9mrn/mnkGBZc2JDadzj1xDIrAhZxCEk3TRWbtNEiYxSHyZcKwIDAQAB";
    public static String PUBLIC_KEY7 = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAzDx0bOCEImfpGAEem47rRWwja1wE04pBVdVwYPorWnL1BBwtdz3p0c6tFXEatrUR3qL8gAhfnVEUJfL68BiR+8KfA0gZK8xVpdO4DZKCT4wQhxo9obHzZzOHNL0beiLKr9gF/d9jLY7oFp8/CDq4Xj+0C5z1fewha1oyZHf0OmHojZktBEcGw7kgY0SGoqx5dIoGbBNQi5Vx06WFcGQC5+BsPH9dRl45U+cXfBp8k+ttfmeqjWUad6TorggisKbWrDcBUbzZ2czzXYqp1RL6gwwpaJdGtAwAWrXPx9mrn/mnkGBZc2JDadzj1xDIrAhZxCEk3TRWbtNEiYxSHyZcKwIDAQAB";
    public static String PUBLIC_KEY8 = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAzDx0bOCEImfpGAEem47rRWwja1wE04pBVdVwYPorWnL1BBwtdz3p0c6tFXEatrUR3qL8gAhfnVEUJfL68BiR+8KfA0gZK8xVpdO4DZKCT4wQhxo9obHzZzOHNL0beiLKr9gF/d9jLY7oFp8/CDq4Xj+0C5z1fewha1oyZHf0OmHojZktBEcGw7kgY0SGoqx5dIoGbBNQi5Vx06WFcGQC5+BsPH9dRl45U+cXfBp8k+ttfmeqjWUad6TorggisKbWrDcBUbzZ2czzXYqp1RL6gwwpaJdGtAwAWrXPx9mrn/mnkGBZc2JDadzj1xDIrAhZxCEk3TRWbtNEiYxSHyZcKwIDAQAB";
    public static String PUBLIC_KEY9 = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAzDx0bOCEImfpGAEem47rRWwja1wE04pBVdVwYPorWnL1BBwtdz3p0c6tFXEatrUR3qL8gAhfnVEUJfL68BiR+8KfA0gZK8xVpdO4DZKCT4wQhxo9obHzZzOHNL0beiLKr9gF/d9jLY7oFp8/CDq4Xj+0C5z1fewha1oyZHf0OmHojZktBEcGw7kgY0SGoqx5dIoGbBNQi5Vx06WFcGQC5+BsPH9dRl45U+cXfBp8k+ttfmeqjWUad6TorggisKbWrDcBUbzZ2czzXYqp1RL6gwwpaJdGtAwAWrXPx9mrn/mnkGBZc2JDadzj1xDIrAhZxCEk3TRWbtNEiYxSHyZcKwIDAQAB";
    public static String PUBLIC_KEY10 = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAzDx0bOCEImfpGAEem47rRWwja1wE04pBVdVwYPorWnL1BBwtdz3p0c6tFXEatrUR3qL8gAhfnVEUJfL68BiR+8KfA0gZK8xVpdO4DZKCT4wQhxo9obHzZzOHNL0beiLKr9gF/d9jLY7oFp8/CDq4Xj+0C5z1fewha1oyZHf0OmHojZktBEcGw7kgY0SGoqx5dIoGbBNQi5Vx06WFcGQC5+BsPH9dRl45U+cXfBp8k+ttfmeqjWUad6TorggisKbWrDcBUbzZ2czzXYqp1RL6gwwpaJdGtAwAWrXPx9mrn/mnkGBZc2JDadzj1xDIrAhZxCEk3TRWbtNEiYxSHyZcKwIDAQAB";
    public static String PUBLIC_KEY11 = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAzDx0bOCEImfpGAEem47rRWwja1wE04pBVdVwYPorWnL1BBwtdz3p0c6tFXEatrUR3qL8gAhfnVEUJfL68BiR+8KfA0gZK8xVpdO4DZKCT4wQhxo9obHzZzOHNL0beiLKr9gF/d9jLY7oFp8/CDq4Xj+0C5z1fewha1oyZHf0OmHojZktBEcGw7kgY0SGoqx5dIoGbBNQi5Vx06WFcGQC5+BsPH9dRl45U+cXfBp8k+ttfmeqjWUad6TorggisKbWrDcBUbzZ2czzXYqp1RL6gwwpaJdGtAwAWrXPx9mrn/mnkGBZc2JDadzj1xDIrAhZxCEk3TRWbtNEiYxSHyZcKwIDAQAB";
    public static String PUBLIC_KEY12 = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAzDx0bOCEImfpGAEem47rRWwja1wE04pBVdVwYPorWnL1BBwtdz3p0c6tFXEatrUR3qL8gAhfnVEUJfL68BiR+8KfA0gZK8xVpdO4DZKCT4wQhxo9obHzZzOHNL0beiLKr9gF/d9jLY7oFp8/CDq4Xj+0C5z1fewha1oyZHf0OmHojZktBEcGw7kgY0SGoqx5dIoGbBNQi5Vx06WFcGQC5+BsPH9dRl45U+cXfBp8k+ttfmeqjWUad6TorggisKbWrDcBUbzZ2czzXYqp1RL6gwwpaJdGtAwAWrXPx9mrn/mnkGBZc2JDadzj1xDIrAhZxCEk3TRWbtNEiYxSHyZcKwIDAQAB";
    public static String PUBLIC_KEY13 = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAzDx0bOCEImfpGAEem47rRWwja1wE04pBVdVwYPorWnL1BBwtdz3p0c6tFXEatrUR3qL8gAhfnVEUJfL68BiR+8KfA0gZK8xVpdO4DZKCT4wQhxo9obHzZzOHNL0beiLKr9gF/d9jLY7oFp8/CDq4Xj+0C5z1fewha1oyZHf0OmHojZktBEcGw7kgY0SGoqx5dIoGbBNQi5Vx06WFcGQC5+BsPH9dRl45U+cXfBp8k+ttfmeqjWUad6TorggisKbWrDcBUbzZ2czzXYqp1RL6gwwpaJdGtAwAWrXPx9mrn/mnkGBZc2JDadzj1xDIrAhZxCEk3TRWbtNEiYxSHyZcKwIDAQAB";
    public static String PUBLIC_KEY14 = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAzDx0bOCEImfpGAEem47rRWwja1wE04pBVdVwYPorWnL1BBwtdz3p0c6tFXEatrUR3qL8gAhfnVEUJfL68BiR+8KfA0gZK8xVpdO4DZKCT4wQhxo9obHzZzOHNL0beiLKr9gF/d9jLY7oFp8/CDq4Xj+0C5z1fewha1oyZHf0OmHojZktBEcGw7kgY0SGoqx5dIoGbBNQi5Vx06WFcGQC5+BsPH9dRl45U+cXfBp8k+ttfmeqjWUad6TorggisKbWrDcBUbzZ2czzXYqp1RL6gwwpaJdGtAwAWrXPx9mrn/mnkGBZc2JDadzj1xDIrAhZxCEk3TRWbtNEiYxSHyZcKwIDAQAB";
    public static String PUBLIC_KEY15 = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAzDx0bOCEImfpGAEem47rRWwja1wE04pBVdVwYPorWnL1BBwtdz3p0c6tFXEatrUR3qL8gAhfnVEUJfL68BiR+8KfA0gZK8xVpdO4DZKCT4wQhxo9obHzZzOHNL0beiLKr9gF/d9jLY7oFp8/CDq4Xj+0C5z1fewha1oyZHf0OmHojZktBEcGw7kgY0SGoqx5dIoGbBNQi5Vx06WFcGQC5+BsPH9dRl45U+cXfBp8k+ttfmeqjWUad6TorggisKbWrDcBUbzZ2czzXYqp1RL6gwwpaJdGtAwAWrXPx9mrn/mnkGBZc2JDadzj1xDIrAhZxCEk3TRWbtNEiYxSHyZcKwIDAQAB";
    public static String PUBLIC_KEY16 = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAzDx0bOCEImfpGAEem47rRWwja1wE04pBVdVwYPorWnL1BBwtdz3p0c6tFXEatrUR3qL8gAhfnVEUJfL68BiR+8KfA0gZK8xVpdO4DZKCT4wQhxo9obHzZzOHNL0beiLKr9gF/d9jLY7oFp8/CDq4Xj+0C5z1fewha1oyZHf0OmHojZktBEcGw7kgY0SGoqx5dIoGbBNQi5Vx06WFcGQC5+BsPH9dRl45U+cXfBp8k+ttfmeqjWUad6TorggisKbWrDcBUbzZ2czzXYqp1RL6gwwpaJdGtAwAWrXPx9mrn/mnkGBZc2JDadzj1xDIrAhZxCEk3TRWbtNEiYxSHyZcKwIDAQAB";
    public static String PUBLIC_KEY17 = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAzDx0bOCEImfpGAEem47rRWwja1wE04pBVdVwYPorWnL1BBwtdz3p0c6tFXEatrUR3qL8gAhfnVEUJfL68BiR+8KfA0gZK8xVpdO4DZKCT4wQhxo9obHzZzOHNL0beiLKr9gF/d9jLY7oFp8/CDq4Xj+0C5z1fewha1oyZHf0OmHojZktBEcGw7kgY0SGoqx5dIoGbBNQi5Vx06WFcGQC5+BsPH9dRl45U+cXfBp8k+ttfmeqjWUad6TorggisKbWrDcBUbzZ2czzXYqp1RL6gwwpaJdGtAwAWrXPx9mrn/mnkGBZc2JDadzj1xDIrAhZxCEk3TRWbtNEiYxSHyZcKwIDAQAB";
    public static String PUBLIC_KEY18 = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAzDx0bOCEImfpGAEem47rRWwja1wE04pBVdVwYPorWnL1BBwtdz3p0c6tFXEatrUR3qL8gAhfnVEUJfL68BiR+8KfA0gZK8xVpdO4DZKCT4wQhxo9obHzZzOHNL0beiLKr9gF/d9jLY7oFp8/CDq4Xj+0C5z1fewha1oyZHf0OmHojZktBEcGw7kgY0SGoqx5dIoGbBNQi5Vx06WFcGQC5+BsPH9dRl45U+cXfBp8k+ttfmeqjWUad6TorggisKbWrDcBUbzZ2czzXYqp1RL6gwwpaJdGtAwAWrXPx9mrn/mnkGBZc2JDadzj1xDIrAhZxCEk3TRWbtNEiYxSHyZcKwIDAQAB";
    public static String PUBLIC_KEY19 = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAzDx0bOCEImfpGAEem47rRWwja1wE04pBVdVwYPorWnL1BBwtdz3p0c6tFXEatrUR3qL8gAhfnVEUJfL68BiR+8KfA0gZK8xVpdO4DZKCT4wQhxo9obHzZzOHNL0beiLKr9gF/d9jLY7oFp8/CDq4Xj+0C5z1fewha1oyZHf0OmHojZktBEcGw7kgY0SGoqx5dIoGbBNQi5Vx06WFcGQC5+BsPH9dRl45U+cXfBp8k+ttfmeqjWUad6TorggisKbWrDcBUbzZ2czzXYqp1RL6gwwpaJdGtAwAWrXPx9mrn/mnkGBZc2JDadzj1xDIrAhZxCEk3TRWbtNEiYxSHyZcKwIDAQAB";
    public static String PUBLIC_KEY20 = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAzDx0bOCEImfpGAEem47rRWwja1wE04pBVdVwYPorWnL1BBwtdz3p0c6tFXEatrUR3qL8gAhfnVEUJfL68BiR+8KfA0gZK8xVpdO4DZKCT4wQhxo9obHzZzOHNL0beiLKr9gF/d9jLY7oFp8/CDq4Xj+0C5z1fewha1oyZHf0OmHojZktBEcGw7kgY0SGoqx5dIoGbBNQi5Vx06WFcGQC5+BsPH9dRl45U+cXfBp8k+ttfmeqjWUad6TorggisKbWrDcBUbzZ2czzXYqp1RL6gwwpaJdGtAwAWrXPx9mrn/mnkGBZc2JDadzj1xDIrAhZxCEk3TRWbtNEiYxSHyZcKwIDAQAB";

    public static void initConfig() {
        try {
            FileInputStream fis = new FileInputStream("Config.properties");
            Properties prop = new Properties();
            prop.load(fis);

            ServerPort = Integer.parseInt(prop.getProperty("ServerPort").trim());

            NotificationServerIP = prop.getProperty("NotificationServerIP").trim();
            NotificationPort = Integer.parseInt(prop.getProperty("NotificationPort").trim());

            UMSServerIP = prop.getProperty("UMSServerIP").trim();
            UMSPort = Integer.parseInt(prop.getProperty("UMSPort").trim());

            MeetPeopleServerIP = prop.getProperty("MeetPeopleServerIP").trim();
            MeetPeopleServerPort = Integer.parseInt(prop.getProperty("MeetPeopleServerPort").trim());

            BuzzServerIP = prop.getProperty("BuzzServerIP").trim();
            BuzzServerPort = Integer.parseInt(prop.getProperty("BuzzServerPort").trim());

            ChatServerIP = prop.getProperty("ChatServerIP").trim();
            ChatServerPort = Integer.parseInt(prop.getProperty("ChatServerPort").trim());

            BackEndServerIp = prop.getProperty("BackEndServerIp").trim();
            BackEndServerPort = Integer.parseInt(prop.getProperty("BackEndServerPort").trim());

            StaticFileServerIp = prop.getProperty("StaticFileServerIp").trim();
            StaticFileServerPort = Integer.parseInt(prop.getProperty("StaticFileServerPort").trim());

            DB_SERVER = prop.getProperty("DB_SERVER").trim();
            DB_PORT = Integer.parseInt(prop.getProperty("DB_PORT").trim());

            SessionTimeOut = Integer.parseInt(prop.getProperty("SessionTimeOut").trim());
            WebSessionTimeOut = Integer.parseInt(prop.getProperty("WebSessionTimeOut", "7").trim());

            MOCOM_IP = prop.getProperty("MOCOM_IP").trim();

            REDIRECT_IOS_APP = prop.getProperty("REDIRECT_IOS_APP").trim();
            REDIRECT_IOS_APP_ENTERPRISE = prop.getProperty("REDIRECT_IOS_APP_ENTERPRISE").trim();
            REDIRECT_ANDROID_APP = prop.getProperty("REDIRECT_ANDROID_APP").trim();
            REDIRECT_ANDROID_STORE = prop.getProperty("REDIRECT_ANDROID_STORE").trim();
            REDIRECT_IOS_7_STORE = prop.getProperty("REDIRECT_IOS_7_STORE").trim();
            ANDROID_PURCHASES_PUBLIC_KEY = prop.getProperty("ANDROID_PURCHASES_PUBLIC_KEY", "").trim();
            AMAZONE_PURCHASES_KEY = prop.getProperty("AMAZONE_PURCHASES_KEY", "").trim();
            SIP_SERVER_IP = prop.getProperty("SIP_SERVER_IP", "").trim();
            //HUNGDT add Multiapp #6374
            PUBLIC_KEY1 = prop.getProperty("PUBLIC_KEY1").trim();
            PUBLIC_KEY2 = prop.getProperty("PUBLIC_KEY2").trim();
            PUBLIC_KEY3 = prop.getProperty("PUBLIC_KEY3").trim();
            PUBLIC_KEY4 = prop.getProperty("PUBLIC_KEY4").trim();
            PUBLIC_KEY5 = prop.getProperty("PUBLIC_KEY5").trim();
            PUBLIC_KEY6 = prop.getProperty("PUBLIC_KEY6").trim();
            PUBLIC_KEY7 = prop.getProperty("PUBLIC_KEY7").trim();
            PUBLIC_KEY8 = prop.getProperty("PUBLIC_KEY8").trim();
            PUBLIC_KEY9 = prop.getProperty("PUBLIC_KEY9").trim();
            PUBLIC_KEY10 = prop.getProperty("PUBLIC_KEY10").trim();
            PUBLIC_KEY11 = prop.getProperty("PUBLIC_KEY11").trim();
            PUBLIC_KEY12 = prop.getProperty("PUBLIC_KEY12").trim();
            PUBLIC_KEY13 = prop.getProperty("PUBLIC_KEY13").trim();
            PUBLIC_KEY14 = prop.getProperty("PUBLIC_KEY14").trim();
            PUBLIC_KEY15 = prop.getProperty("PUBLIC_KEY15").trim();
            PUBLIC_KEY16 = prop.getProperty("PUBLIC_KEY16").trim();
            PUBLIC_KEY17 = prop.getProperty("PUBLIC_KEY17").trim();
            PUBLIC_KEY18 = prop.getProperty("PUBLIC_KEY18").trim();
            PUBLIC_KEY19 = prop.getProperty("PUBLIC_KEY19").trim();
            PUBLIC_KEY20 = prop.getProperty("PUBLIC_KEY20").trim();

        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
    }

}
