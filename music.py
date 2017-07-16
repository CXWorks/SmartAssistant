# -*- coding: utf-8 -*-
import sys
def searchAndDownload(keyword):
    import pprint
    from NEMbox import api
    import requests
    import shutil
    pp = pprint.PrettyPrinter()
    nb = api.NetEase()
    list=nb.search(keyword)

    sid=list['result']['songs'][0]['id']
    pp.pprint(sid)

    sd=nb.songs_detail_new_api([sid])
    url=sd[0]['url']
    pp.pprint(url)
    response=requests.request(method='GET',url=url,stream=True)
    with open('songs/%s.mp3' % keyword, 'wb') as out_file:
        shutil.copyfileobj(response.raw, out_file)
    del response


if __name__ == '__main__':
    args=sys.argv
    if(len(args)<2):
        song="firework"
    else:
        song=""
        for word in args[1:]:
            song+=word+" "
    searchAndDownload(song.strip())
    exit(0)

