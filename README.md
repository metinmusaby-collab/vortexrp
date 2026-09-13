# RPCore - Minecraft RP Simulasyon Eklentisi

Sunucu surumu: 1.21.11 (Paper), Java 21+ (Java 25 runtime ile de calisir).

## GitHub Actions ile Derleme (Onerilen)

Bu depoda `.github/workflows/build.yml` hazir geliyor. PaperMC deposuna bu ortamdan
erisilemedigi icin jar, GitHub'in kendi sunucularinda (tam internet erisimli) derlenir.

### Adimlar

1. GitHub'da bos bir repo olustur (orn. `rpcore-plugin`), **public ya da private** olabilir.
2. Bu klasoru o reponun icerigiyle doldurup gonder:

   ```bash
   cd rpplugin
   git init
   git add .
   git commit -m "RPCore ilk surum"
   git branch -M main
   git remote add origin https://github.com/KULLANICI_ADIN/rpcore-plugin.git
   git push -u origin main
   ```

3. GitHub'da reponun **Actions** sekmesine git. `Build RPCore` workflow'u otomatik
   calisacak (push sonrasi). Bitince acilan calistirmaya tikla.
4. Sayfanin altindaki **Artifacts** bolumunden `RPCore-jar` dosyasini indir, icinden
   `RPCore.jar` cikacak.
5. `RPCore.jar` dosyasini sunucunun `plugins/` klasorune koy, sunucuyu baslat.

Workflow her `git push` sonrasi otomatik tekrar calisir, yani kodda degisiklik
yapip tekrar push ettiginde yeni jar otomatik uretilir.

### Elle tetikleme

Actions sekmesinde workflow'u sectikten sonra sag ustte **Run workflow** butonuyla
push yapmadan da manuel calistirabilirsin (workflow_dispatch destekleniyor).

## Yerel Derleme (internet erisimi olan bir bilgisayarda)

```bash
mvn clean package
```

`target/RPCore.jar` olusur.

## Komutlar

- `/rp set <ev|isyeri|restoran|market> <ad> [fiyat] [kira]` - admin, konum kaydi
- `/rp remove <ad>`, `/rp list`, `/rp tp <ad>`
- `/ev`, `/isyeri`, `/market`, `/restoran` `[menu]`
- `/is [birak]` - meslek sec (Ciftci, Madenci, Balikci, Polis)
- `/polis` - nobet ac/kapat
- `/banka [gonder <oyuncu> <miktar>]`
- `/araba [cagir|park]`
- `/takim kur|katil|ayril <isim>`
- `/lig` - puan durumu
- `/telefon` - ana menu (tum sistemlere hizli erisim + sosyal medya)
- `/sosyal <mesaj>`
- `/su` - su ic (susamayi azalt)
