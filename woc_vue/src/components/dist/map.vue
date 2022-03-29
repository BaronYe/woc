<template>
  <div :style="{width}">
    <div class="map-search">
      <input
          v-model.trim="search"
          class="search"
          type="text"
          placeholder="输入搜索城市"
          @keyup.enter="getLatLngBounds"
      />
      <div class="button" @click="getLatLngBounds">搜索</div>
    </div>

    <div id="map" :style="{width, height}"></div>
  </div>
</template>

<script>
export default {
  props: {
    //地图key
    mapKey: {
      type: String,
      default: 'KZOBZ-MHMWG-IVJQG-IICXA-E3FFS-TKBZR'
    },
    //设置地图宽度
    width: {
      type: String,
      default: '500px'
    },
    //设置地图高度
    height: {
      type: String,
      default: '200px'
    },
  },
  data() {
    return {
      latLng: [39.916527, 116.397128], //默认位置，之后会自动定位到当前位置。

      search: "", //搜索关键字
      marker: null, //标记点
      map: null, //地图实例

      //获取周边区域定时器
      setTime: null,
      selectPosition: -1,  //选择的位置
      BoundsPois: [], //周边列表
    };
  },

  mounted() {
    this.loadScript();
  },

  methods: {
    //异步加载地图js
    loadScript() {
      var script = document.createElement("script");
      script.type = "text/javascript";
      script.charset = "utf-8";
      //地图js加载完成的回调，初始化地图
      window.initMap = () => {
        this.init();
      };
      script.src =
          "https://map.qq.com/api/js?v=2.exp&key=" +
          this.mapKey +
          "&callback=initMap";
      document.body.appendChild(script);
    },

    //初始化地图
    init() {
      this.map = new qq.maps.Map(document.getElementById("map"), {
        center: new qq.maps.LatLng(this.latLng[0], this.latLng[1]), //设置地图中心的
        keyboardShortcuts: false,  //禁止键盘操控
        disableDefaultUI: true,    //禁止所有控件
        zoom: 16
      });
      //获取当前位置
      this.getLocation();

      //绑定地图中心点改变事件
      qq.maps.event.addListener(this.map, "center_changed", e => {
        if(this.selectPosition != -1) return;
        this.centerChanged(e);
      });
      //绑定地图开始拖动事件
      qq.maps.event.addListener(this.map, "dragstart", e => {
        //重置选择、搜索框
        this.search = '';
        this.selectPosition = -1;
      });
      //绑定地图缩放级别更改事件
      qq.maps.event.addListener(this.map, "zoom_changed", e => {
        //重置选择、搜索框
        this.search = '';
        this.selectPosition = -1;
      });
    },

    //点击地图，获取定位度
    getLatLng(e) {
      this.$emit("getLatLng", [e.latLng.lat, e.latLng.lng]);
    },

    //地图中心点改变事件
    centerChanged(e, type) {
      let center = this.map.getCenter();
      if (this.marker) {
        //设置标注点为中心点
        this.marker.setPosition(center);
        //设置标注点下落动画
        this.marker.setAnimation(qq.maps.MarkerAnimation.DOWN);
        clearTimeout(this.setTime);
        this.setTime = setTimeout(() => {
          this.getAddress();
        }, 500)
      }
    },

    //查询位置
    searchCity() {
      let geocoder = new qq.maps.Geocoder({
        complete: result => {
          this.map.setCenter(result.detail.location);
          //设置标注点位置
          this.marker.setPosition(result.detail.location);
          //设置标注点下落动画
          this.marker.setAnimation(qq.maps.MarkerAnimation.DOWN);
        }
      });
      geocoder.getLocation(this.search);
    },

    //搜索位置，查询周边位置信息
    getLatLngBounds(init) {
      this.selectPosition = -1;
      let latlngBounds = new qq.maps.LatLngBounds();
      //调用Poi检索类
      let searchService = new qq.maps.SearchService({
        complete: results => {
          this.BoundsPois = results.detail.pois;
          this.map.setCenter(this.BoundsPois[0].latLng);
          //设置标注点为中心点
          this.marker.setPosition(this.BoundsPois[0].latLng);
        }
      });
      searchService.searchInBounds(this.search, latlngBounds); //根据范围和关键字进行指定区域检索。
    },

    //根据经纬度进行位置解析
    getAddress(latLng) {
      let geocoder = new qq.maps.Geocoder({
        complete: result => {
          this.$emit("toMap",result.detail.nearPois[0]);
        }
      });
      geocoder.getAddress(latLng || this.map.getCenter());
    },

    //获取当前位置
    getLocation() {
      let cs = new qq.maps.CityService({
        map: this.map,
        complete: results => {
          this.latLng = [results.detail.latLng.lat, results.detail.latLng.lng];
          //设置地图中心位置为当前定位。
          this.map.setCenter(results.detail.latLng);
          //设置标注点为当前中心位置
          this.setMarker(results.detail.latLng);
          //获取当前位置的周边范围
          this.getAddress();
        }
      });
      //调用searchLocalCity()方法获取位置
      cs.searchLocalCity();
    },

    //设置底部标记点
    setMarker(center) {
      let icon = new qq.maps.MarkerImage(
          "data:image/png;base64,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",
          new qq.maps.Size(52, 73),
          new qq.maps.Point(0, 0),
          new qq.maps.Point(12, 34),
          new qq.maps.Size(25, 35)
      );
      let iconb = new qq.maps.MarkerImage(
          "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQAgMAAABinRfyAAAADFBMVEUAAAD///////8Aef/rgTCIAAAAAnRSTlMAlm//+0kAAAAlSURBVAjXY4AAtglAgvsBBiG1+tUSBv3//3+ACQgXUx3EADAAAAKHE3VhGPlFAAAAAElFTkSuQmCC",
          new qq.maps.Size(16, 16),
          new qq.maps.Point(0, 0),
          new qq.maps.Point(5, 5),
          new qq.maps.Size(10, 10)
      );

      this.marker = new qq.maps.Marker({
        map: this.map,
        position: center
      });
      this.marker.setIcon(icon);
      this.marker.setShadow(iconb);
    }
  }
};
</script>

<style lang="less" scoped>
.map-search {
  display: flex;
  padding-bottom: 20px;
  .search {
    width: 390px;
    height: 32px;
    line-height: 32px;
    padding: 0 15px;
    color: #606266;
    border-radius: 4px;
    border: 1px solid #dcdfe6;
    background-color: #fff;
    transition: border-color 0.2s cubic-bezier(0.645, 0.045, 0.355, 1);
    outline: none;
    &::-webkit-input-placeholder {
      color: #c0c4cc;
    }
  }
  .button {
    cursor: pointer;
    text-align: center;
    outline: none;
    margin: 0 0 0 10px;
    transition: 0.1s;
    font-size: 12px;
    border-radius: 4px;
    width: 66px;
    height: 32px;
    line-height: 32px;
    color: var(--white);
    background-color: var(--orange);
    &:hover:active {
      opacity: 1;
    }
    &:hover {
      opacity: 0.8;
    }
  }
}
</style>
