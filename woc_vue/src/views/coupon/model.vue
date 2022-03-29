<template>
  <div>
    <el-drawer
        :title="title"
        :visible.sync="dialogFormVisible"
        size="50%"
        :wrapperClosable="false"
        direction="rtl">
      <div class="simon-drawer__content">
        <v-prompt :title="[
          '标识“*”的选项为必填项，其余为选填项',
          '固定时间：选择结束日期，所有领取的优惠券有效期都截止在结束日期当天',
          '自领取之日起：填写一个数字，表示从领取之日起有效期持续多少天',
          '选择全部店铺及通用券，选择指定店铺及专用券',
          '指定店铺时“限时抢“是判断该优惠券是不是要加入专用券的限时抢专区'
        ]">
        </v-prompt>
        <el-form label-position="right" :model="form" :rules="rules" ref="formRef" v-if="form != null"
                 :label-width="formLabelWidth">
          <el-form-item label="优惠券名称：" prop="title">
            <el-input v-model="form.title" style="width: 450px" ></el-input>
          </el-form-item>
          <el-form-item label="优惠券发放时间：" prop="startTime">
            <el-date-picker
                v-model="form.startTime"
                type="datetime"
                format="yyyy 年 MM 月 dd 日 HH:mm"
                value-format="yyyy-MM-dd HH:mm"
                style="width: 250px;"
                placeholder="选择日期时间">
            </el-date-picker>
          </el-form-item>
          <el-form-item label="优惠券截止时间：" prop="endTime" >
            <el-date-picker
                v-model="form.endTime"
                type="datetime"
                format="yyyy 年 MM 月 dd 日 HH:mm"
                value-format="yyyy-MM-dd HH:mm"
                style="width: 250px;"
                placeholder="选择日期时间">
            </el-date-picker>
          </el-form-item>
          <el-form-item label="优惠券类型：" >
            <div class="layui-input-block">
              <button class="layui-btn" :class="[form.types == '0'?'ns-exchange-selected':'']" @click="form.types = '0'">满减</button>
              <button class="layui-btn" :class="[form.types == '1'?'ns-exchange-selected':'']" @click="form.types = '1'">折扣</button>
            </div>
          </el-form-item>
          <el-form-item label="优惠券面额：" prop="couponPrice" v-if="form.types == '0'">
            <el-input-number v-model="form.couponPrice"  style="width: 140px" controls-position="right" :min="1" ></el-input-number>
            <span class="layui-form-mid">元</span>
          </el-form-item>
          <div class="ns-word-aux" v-if="form.types == '0'">
            <p>价格不能小于0</p>
          </div>
          <el-form-item label="优惠券折扣：" prop="discount" v-if="form.types == '1'">
            <el-input-number v-model="form.discount"  style="width: 140px" controls-position="right" :step="0.1" :max="10" :min="1" ></el-input-number>
            <span class="layui-form-mid">折</span>
          </el-form-item>
          <div class="ns-word-aux" v-if="form.types == '1'">
            <p>优惠券折扣不能小于1折，且不可大于9.9折</p>
          </div>
          <el-form-item label="发放数量：" prop="totalCount" >
            <el-input-number v-model="form.totalCount"  style="width: 140px" controls-position="right" :min="1" ></el-input-number>
            <span class="layui-form-mid">张</span>
          </el-form-item>
          <el-form-item label="满多少元可以使用：" prop="useMinPrice">
            <el-input-number v-model="form.useMinPrice" style="width: 140px" controls-position="right"
                             :min="0"></el-input-number>
            <span class="layui-form-mid">元</span>
          </el-form-item>
          <div class="ns-word-aux">
            <p>价格为”0”时则是无门槛</p>
          </div>
          <el-form-item label="有效期类型：">
            <el-radio v-model="form.timeStatus" label="0">固定时间</el-radio>
            <el-radio v-model="form.timeStatus" label="1">领取之日起</el-radio>
          </el-form-item>
          <el-form-item label="活动结束时间：" prop="couponTime" v-if="form.timeStatus == '0'">
            <el-date-picker
                v-model="form.couponTime"
                type="datetime"
                format="yyyy 年 MM 月 dd 日 HH:mm"
                value-format="yyyy-MM-dd HH:mm"
                style="width: 250px;"
                placeholder="选择日期时间">
            </el-date-picker>
          </el-form-item>
          <el-form-item label="领取之日起：" prop="couponDays" v-if="form.timeStatus == '1'">
            <el-input-number v-model="form.couponDays"  style="width: 140px" controls-position="right" :min="0" ></el-input-number>
            <span class="layui-form-mid">天有效</span>
          </el-form-item>
          <el-form-item label="活动店铺：">
            <el-radio v-model="form.shopStatus" label="0">全部店铺参与</el-radio>
            <el-radio v-model="form.shopStatus" label="1">指定店铺参与</el-radio>
          </el-form-item>
          <el-form-item label="选择店铺：" prop="shopName" v-if="form.shopStatus == '1'">
            <el-select v-model="form.shopName" style="width: 220px" no-data-text="加载中"  placeholder="请选择店铺" @click.native="toShopBox()"></el-select>
          </el-form-item>
          <el-form-item label="是否开启限时抢：" v-if="form.shopStatus == '1'">
            <el-radio v-model="form.isRob" label="0">关闭</el-radio>
            <el-radio v-model="form.isRob" label="1">开启</el-radio>
          </el-form-item>
        </el-form>
        <div class="simon-drawer__footer">
          <el-button @click="dialogFormVisible=false">取 消</el-button>
          <el-button class="simon-button" @click="setClick('formRef')">确 定</el-button>
        </div>
      </div>
    </el-drawer>
    <v-shop-box ref="shopBox" @send="doSend"></v-shop-box>
  </div>
</template>

<script>

import prompt from '@components/dist/prompt';
import shopBox from '@components/dist/shopBox';

export default {
  name: 'detail',
  components:{
    'v-prompt': prompt,
    'v-shop-box':shopBox
  },
  data() {
    return {
      title: '优惠券编辑',
      dialogFormVisible: false,
      id: 0,
      form: null,
      formLabelWidth: '200px',
      rules: {
        title: [{ required: true, message: '请填写优惠券名称' }],
        startTime: [{ required: true, message: '请选择优惠券发放时间' }],
        endTime: [{ required: true, message: '请选择优惠截止时间' }],
        couponPrice: [{ required: true, message: '请输入优惠券面额' }],
        discount: [{ required: true, message: '请输入优惠券折扣' }],
        totalCount: [{ required: true, message: '请输入优惠券数量' }],
        couponTime: [{ required: true, message: '请选择活动结束时间' }],
        couponDays: [{ required: true, message: '请输入有效天数' }],
        shopName: [{ required: true, message: '请选择店铺' }],
      },

    };
  },
  mounted() {

  },
  methods: {
    // 显示
    isShowDia(id, isVisible) {
      this.id = id;
      this.dialogFormVisible = isVisible;
      if (id == 0) {
        this.form = {
          id: 0,
          title:"",
          types:"0",
          couponPrice:1,
          discount:1,
          totalCount:1,
          useMinPrice:0,
          timeStatus:"0",
          couponTime:"",
          couponDays:1,
          startTime:"",
          endTime:"",
          shopStatus:"0",
          shopId:0,
          shopType:"",
          shopName:"",
          isRob:"0"
        };
      }else{
        this.toDetail();
      }
    },
    // 保存
    setClick(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.api.setCoupon({
            id:this.form.id,
            title:this.form.title,
            couponPrice:parseInt(this.form.couponPrice),
            discount:this.form.discount + "",
            totalCount:parseInt(this.form.totalCount),
            useMinPrice:parseInt(this.form.useMinPrice),
            couponDays:parseInt(this.form.couponDays),
            shopId:parseInt(this.form.shopId),
            timeStatus:this.form.timeStatus,
            types:this.form.types,
            couponTime:this.form.couponTime,
            sTime:this.form.startTime,
            eTime:this.form.endTime,
            shopStatus:this.form.shopStatus,
            shopType:this.form.shopType,
            shopName:this.form.shopName,
            isRob:this.form.isRob,
          }).then(res=>{
            this.$message.success('保存成功');
            setTimeout(() => {
              this.dialogFormVisible = !this.dialogFormVisible;
              this.$parent.getCouponFn();
            }, 1500);
          })
        }
      });
    },
    //获取活动详情
    toDetail(){
      this.api.getCouponById({
        id:this.id
      }).then(res=>{
        this.form = res.data;
      })
    },
    // 选择店铺
    toShopBox(){
      this.$refs['shopBox'].isShowDia();
    },
    //获取到子组件传入的值
    doSend(e){
      this.form.shopType = e.types;
      this.form.shopName = e.name;
      this.form.shopId = e.id;
    },
  }
};
</script>

<style scoped lang="less">
/deep/ .el-image-viewer__close {
  color: #fff;
  top: 100px;
  right: 100px;
}

.simon-drawer__content {
  display: flex;
  flex-direction: column;
  height: 100%;

  form {
    flex: 1;
  }

  .simon-drawer__footer {
    display: flex;
    margin-top: 40px;
    padding-bottom: 10px;

    button {
      flex: 1;
    }
  }
}

/deep/ .el-drawer__body {
  overflow-y: auto;
  overflow-x: hidden;
  padding: 20px 20px 10px;
}

/deep/ .el-upload--text {
  width: 100%;
  height: 100%;
  border: none;
}

/deep/ .el-tabs__item.is-active{
  color: var(--orange);
}
/deep/.el-tabs__active-bar{
  background-color: var(--orange);
}
/deep/.el-tabs__item:hover{
  color: var(--orange);
}
.avatar-uploader-banner {
  width: 82px;
  height: 82px;
  line-height: 82px;
  border: 1px #ccc dashed;
  border-radius: 4px;

}
.banner {
  width: 82px;
  height: 82px;
  position: relative;
  margin-left: 10px;
  margin-bottom: 10px;
  border-radius: 4px;
}

.banner-div {
  position: absolute;
  width: 82px;
  height: 82px;
  background: rgba(0, 0, 0, .6);
  text-align: center;
  line-height: 82px;
  display: none;
  border-radius: 4px;
}

.banner:hover .banner-div {
  display: block;
}

.banner-div i {
  color: #fff;
}

.banner-img {
  width: 82px;
  height: 82px;
}
/deep/.el-radio__label{
  font-size: 14px;
  font-weight: 400;
  color: #666;
}
/deep/.el-radio__input.is-checked+.el-radio__label{
  color: var(--orange);
}
/deep/.el-radio__input.is-checked .el-radio__inner{
  border-color: var(--orange);
  background:var(--orange);
}
.avatar-uploader {
  width: 82px;
  height: 82px;
  line-height: 82px;
  border: 1px #ddd dashed;
  margin-left: 5px;
}

.avatar {
  width: 82px;
  height: 82px;
  display: block;
}

.ns-exchange-selected {
  border: 1px solid #ff8143 !important;
}
.ns-exchange-selected:after {
  content: "";
  display: inline-block;
  width: 20px;
  height: 20px;
  background-image: url(https://v4.niuteam.cn/public/static/img/selected.png);
  position: absolute;
  bottom: 0;
  right: 0;
}
.layui-btn {
  display: inline-block;
  height: 34px;
  line-height: 34px;
  font-size: 14px;
  padding: 0 20px;
  position: relative;
  border: 1px solid #C9C9C9;
  background-color: #fff;
  color: #555;
  white-space: nowrap;
  text-align: center;
  border-radius: 2px;
  cursor: pointer;
}
.layui-btn+.layui-btn {
  margin-left: 10px;
}
.layui-form-mid {
  display: inline-block;
  height: 34px;
  line-height: 34px;
  padding: 0 !important;
  margin-left: 10px;
}
.ns-word-aux{
  margin-left: 200px;
  display: block;
  margin-top: 5px;
  color: #B2B2B2;
  font-size: 12px;
  line-height: 1;
  position: relative;
  top: -10px;
}
</style>
